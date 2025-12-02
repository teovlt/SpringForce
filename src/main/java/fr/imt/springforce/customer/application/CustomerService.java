package fr.imt.springforce.customer.application;

import fr.imt.springforce.common.exception.ValidationException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepositoryPort customerRepositoryPort;
    private final CustomerMapper customerMapper;

    public CustomerDetails save(@Valid CustomerDetails customerDetails) {
        List<String> allErrors = new ArrayList<>();

        if (customerDetails.getAddress() != null) {
            Set<String> addressErrors = AddressValidator.validate(customerDetails.getAddress());
            if (!addressErrors.isEmpty()) {
                allErrors.add("Invalid address: " + String.join(", ", addressErrors));
            }
        }

        Set<String> licenceErrors = LicenceValidator.validate(customerDetails.getLicenceNumber());
        if (!licenceErrors.isEmpty()) {
            allErrors.add("Invalid licence number: " + String.join(", ", licenceErrors));
        }

        if (!allErrors.isEmpty()) {
            throw new ValidationException(String.join("; ", allErrors));
        }

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
        return customerMapper.toCustomerDetails(savedCustomer);
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
        List<String> allErrors = new ArrayList<>();

        if (customerDetails.getAddress() != null) {
            Set<String> addressErrors = AddressValidator.validate(customerDetails.getAddress());
            if (!addressErrors.isEmpty()) {
                allErrors.add("Invalid address: " + String.join(", ", addressErrors));
            }
        }
        Set<String> licenceErrors = LicenceValidator.validate(customerDetails.getLicenceNumber());
        if (!licenceErrors.isEmpty()) {
            allErrors.add("Invalid licence number: " + String.join(", ", licenceErrors));
        }

        if (!allErrors.isEmpty()) {
            throw new ValidationException(String.join("; ", allErrors));
        }

        Customer existingCustomer = customerRepositoryPort.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id.toString()));

        existingCustomer.setFirstName(customerDetails.getFirstName());
        existingCustomer.setFamilyName(customerDetails.getFamilyName());
        existingCustomer.setEmail(customerDetails.getEmail());
        existingCustomer.setPhoneNumber(customerDetails.getPhoneNumber());
        existingCustomer.setAddress(customerDetails.getAddress());
        existingCustomer.setLicenceNumber(customerDetails.getLicenceNumber());

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
