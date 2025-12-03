package fr.imt.springforce.customer.domain.policies;

import fr.imt.springforce.common.validation.ValidationResult;
import fr.imt.springforce.common.validation.Validator;
import fr.imt.springforce.customer.api.CustomerDetails;
import fr.imt.springforce.customer.domain.Customer;
import fr.imt.springforce.customer.domain.port.out.CustomerRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UniquenessValidator implements Validator<CustomerDetails> {

    private final CustomerRepositoryPort customerRepository;

    @Override
    public void validate(CustomerDetails customerDetails, ValidationResult result) {
        validateEmailUniqueness(customerDetails, result);
        validateNameUniqueness(customerDetails, result);
        validateLicenceNumberUniqueness(customerDetails, result);
    }

    private void validateEmailUniqueness(CustomerDetails customerDetails, ValidationResult result) {
        Optional<Customer> customer = customerRepository.findByEmail(customerDetails.getEmail());
        if (customer.isPresent()) {
            result.addError("User with this email already exists. Please choose a unused email");
        }
    }

    private void validateNameUniqueness(CustomerDetails customerDetails, ValidationResult result) {
        if (customerRepository.existsByFirstNameAndFamilyNameAndBirthDate(
                customerDetails.getFirstName(),
                customerDetails.getFamilyName(),
                customerDetails.getBirthDate()
        )) {
            result.addError("A user with the same first name, last name, and birth date already exists.");
        }
    }

    private void validateLicenceNumberUniqueness(CustomerDetails customerDetails, ValidationResult result) {
        if (customerRepository.existsByLicenceNumber(customerDetails.getLicenceNumber())) {
            result.addError("A user with the licence number already exist.");

        }
    }

}