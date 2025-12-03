package fr.imt.springforce.customer.application;

import fr.imt.springforce.customer.api.CustomerDetails;
import fr.imt.springforce.customer.api.CustomerNotFoundException;
import fr.imt.springforce.customer.application.mapper.CustomerMapper;
import fr.imt.springforce.customer.domain.Customer;
import fr.imt.springforce.customer.domain.port.out.CustomerRepositoryPort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepositoryPort customerRepositoryPort;
    private final CustomerMapper customerMapper;

    public CustomerDetails save(@Valid CustomerDetails customerDetails) {
        Customer customer = customerRepositoryPort.save(Customer.generate(
                customerDetails.getFirstName(), customerDetails.getFamilyName(), customerDetails.getEmail(), customerDetails.getPhoneNumber()
        ));
        return customerMapper.toCustomerDetails(customer);
    }

    public CustomerDetails findById(UUID id) {
        Customer customer = customerRepositoryPort.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id.toString()));
        return customerMapper.toCustomerDetails(customer);
    }

    public Collection<CustomerDetails> findAll() {
        Collection<Customer> customers = customerRepositoryPort.findAll();
        return customers.stream()
                .map(customerMapper::toCustomerDetails)
                .toList();
    }

    public CustomerDetails update(UUID id, @Valid CustomerDetails customerDetails) {
        Customer existingCustomer = customerRepositoryPort.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id.toString()));

        existingCustomer.setFirstName(customerDetails.getFirstName());
        existingCustomer.setFamilyName(customerDetails.getFamilyName());
        existingCustomer.setEmail(customerDetails.getEmail());
        existingCustomer.setPhoneNumber(customerDetails.getPhoneNumber());

        customerRepositoryPort.save(existingCustomer);
        return customerMapper.toCustomerDetails(existingCustomer);
    }

    public void delete(UUID id) {
        if (!customerRepositoryPort.existsById(id)) {
            throw new CustomerNotFoundException(id.toString());
        }
        customerRepositoryPort.deleteById(id);
    }

}
