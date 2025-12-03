/*
 * -----------------------------------------------------------------
 *  Ce code source est la propriété de Boulanger S.A. Tous droits réservés, 2025.
 *  (C) Copyright Boulanger S.A., 2025
 * -----------------------------------------------------------------
 */
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
        Optional<Customer> customer = customerRepository.findByEmail(customerDetails.getEmail());
        if (customer.isPresent()) {
            result.addError("User with this email already exists. Please choose a unused email");
        }
    }

}
