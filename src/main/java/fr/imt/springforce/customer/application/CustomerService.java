package fr.imt.springforce.customer.application;

import fr.imt.springforce.common.validation.ValidationChain;
import fr.imt.springforce.customer.api.CustomerClient;
import fr.imt.springforce.customer.api.CustomerDetails;
import fr.imt.springforce.customer.api.CustomerNotFoundException;
import fr.imt.springforce.customer.application.mapper.CustomerMapper;
import fr.imt.springforce.customer.domain.Customer;
import fr.imt.springforce.customer.domain.policies.AddressValidator;
import fr.imt.springforce.customer.domain.policies.LicenceValidator;
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

        Customer customer = Customer.generate(
                customerDetails.getFirstName(),
                customerDetails.getFamilyName(),
                customerDetails.getEmail(),
                customerDetails.getPhoneNumber(),
                customerDetails.getAddress(),
                customerDetails.getLicenceNumber()
        );
        customer.setAddress(customerDetails.getAddress());

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

        existingCustomer.setFirstName(customerDetails.getFirstName());
        existingCustomer.setFamilyName(customerDetails.getFamilyName());
        existingCustomer.setEmail(customerDetails.getEmail());
        existingCustomer.setPhoneNumber(customerDetails.getPhoneNumber());
        existingCustomer.setAddress(customerDetails.getAddress());
        existingCustomer.setLicenceNumber(customerDetails.getLicenceNumber());

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
                (details, result) -> new LicenceValidator().validate(details.getLicenceNumber(), result)
        ).validate(customerDetails);
    }
}
