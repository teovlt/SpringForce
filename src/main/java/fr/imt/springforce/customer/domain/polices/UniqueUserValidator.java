package fr.imt.springforce.customer.domain.polices;

import fr.imt.springforce.common.ValidationResult;
import fr.imt.springforce.common.ValidationHandler;
import fr.imt.springforce.common.ValidatorService;
import fr.imt.springforce.customer.api.CustomerDetails;
import fr.imt.springforce.customer.domain.Customer;
import fr.imt.springforce.customer.domain.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

// RG : user must be unique by (email, firstName, LastName)

@Service
@RequiredArgsConstructor
public class UniqueUserValidator implements ValidatorService<CustomerDetails> {

    private final CustomerRepository customerRepository;

    private final ValidationHandler<CustomerDetails> customerRequ;

    @Override
    public ValidationResult validate(CustomerDetails customerDetails) {
        Optional<Customer> customer = customerRepository.findByEmail(customerDetails.getEmail());
        if (customer.isPresent()) {
            return ValidationResult.invalid(String.format("User with email %s already exists", customerDetails.getEmail()), "CUSTOMER_ALREADY_EXISTS");
        }
        return ValidationResult.valid();
    }

}
