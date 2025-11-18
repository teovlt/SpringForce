package fr.imt.springforce.customer.domain;

import fr.imt.springforce.customer.api.CustomerDetails;
import fr.imt.springforce.customer.api.CustomerNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerDetails save(@Valid CustomerDetails customer) {
        customerRepository.save(Customer.generate(
                customer.getFirstName(), customer.getFamilyName(), customer.getEmail(), customer.getPhoneNumber()
        ));
        return customer;
    }

    public CustomerDetails findById(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        return customerMapper.toCustomerDetails(customer);
    }

    public Collection<CustomerDetails> findAll() {
        Collection<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(customerMapper::toCustomerDetails)
                .toList();
    }

    public CustomerDetails update(UUID id, @Valid CustomerDetails customerDetails) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id.toString()));

        existingCustomer.setFirstName(customerDetails.getFirstName());
        existingCustomer.setFamilyName(customerDetails.getFamilyName());
        existingCustomer.setEmail(customerDetails.getEmail());
        existingCustomer.setPhoneNumber(customerDetails.getPhoneNumber());

        customerRepository.save(existingCustomer);
        return customerMapper.toCustomerDetails(existingCustomer);
    }

    public void delete(UUID id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException(id.toString());
        }
        customerRepository.deleteById(id);
    }

}

