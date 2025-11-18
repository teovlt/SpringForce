/*
 * -----------------------------------------------------------------
 *  Ce code source est la propriété de Boulanger S.A. Tous droits réservés, 2025.
 *  (C) Copyright Boulanger S.A., 2025
 * -----------------------------------------------------------------
 */
package fr.imt.carleasesystem.customer.application;

import fr.imt.carleasesystem.customer.api.CustomerDetails;
import fr.imt.carleasesystem.customer.api.CustomerNotFoundException;
import fr.imt.carleasesystem.customer.domain.Customer;
import fr.imt.carleasesystem.customer.domain.CustomerMapper;
import fr.imt.carleasesystem.customer.domain.CustomerRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
                .collect(Collectors.toList());
    }

}

