package fr.imt.springforce.customer.application;

import fr.imt.springforce.common.validation.ValidationChain;
import fr.imt.springforce.contract.business.mapper.ContractMapper;
import fr.imt.springforce.customer.api.CustomerClient;
import fr.imt.springforce.customer.api.CustomerDetails;
import fr.imt.springforce.customer.api.CustomerNotFoundException;
import fr.imt.springforce.customer.application.mapper.CustomerMapper;
import fr.imt.springforce.customer.domain.Customer;
import fr.imt.springforce.customer.domain.CustomerId;
import fr.imt.springforce.customer.domain.policies.AddressValidator;
import fr.imt.springforce.customer.domain.policies.LicenceValidator;
import fr.imt.springforce.customer.domain.policies.UniquenessValidator;
import fr.imt.springforce.customer.domain.port.out.CustomerRepositoryPort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
class CustomerService implements CustomerClient {

    private final CustomerRepositoryPort customerRepositoryPort;
    private final CustomerMapper customerMapper;

    @Override
    public Optional<CustomerDetails> save(@Valid CustomerDetails customerDetails) {
        validateCustomerDetails(customerDetails);

        Customer customer = customerMapper.toCustomer(customerDetails);
        customer.setCustomerId(CustomerId.generate());

        Customer savedCustomer = customerRepositoryPort.save(customer);
        return Optional.of(customerMapper.toCustomerDetails(savedCustomer));
    }

    @Override
    public Optional<CustomerDetails> findById(UUID id) {
        return customerRepositoryPort.findById(id)
                .map(customerMapper::toCustomerDetails);
    }

    @Override
    public Collection<CustomerDetails> findAll() {
        Collection<Customer> customers = customerRepositoryPort.findAll();
        return customers.stream()
                .map(customerMapper::toCustomerDetails)
                .toList();
    }

    @Override
    public Optional<CustomerDetails> update(@Valid CustomerDetails customerDetails, UUID id) {
        validateCustomerDetails(customerDetails);

        Customer existingCustomer = customerRepositoryPort.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id.toString()));

        customerMapper.updateCustomerFromDetails(customerDetails, existingCustomer);

        customerRepositoryPort.save(existingCustomer);
        return Optional.of(customerMapper.toCustomerDetails(existingCustomer));
    }

    @Override
    public void delete(UUID id) {
        if (!customerRepositoryPort.existsById(id)) {
            throw new CustomerNotFoundException(id.toString());
        }
        customerRepositoryPort.deleteById(id);
    }

    private void validateCustomerDetails(CustomerDetails customerDetails) {
        ValidationChain.<CustomerDetails>of(
                (details, result) -> {
                    if (details.getAddress() != null) {
                        new AddressValidator().validate(details.getAddress(), result);
                    }
                },
                (details, result) -> new LicenceValidator().validate(details.getLicenceNumber(), result),
                new UniquenessValidator(customerRepositoryPort)
        ).validate(customerDetails);
    }

}
