package fr.imt.springforce.customer.domain.policies;

import fr.imt.springforce.common.validation.ValidationResult;
import fr.imt.springforce.customer.api.CustomerDetails;
import fr.imt.springforce.customer.domain.Customer;
import fr.imt.springforce.customer.domain.port.out.CustomerRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UniquenessValidatorTest {

    @Mock
    private CustomerRepositoryPort customerRepositoryPort;

    private UniquenessValidator validator;

    private CustomerDetails testCustomerDetails;

    @BeforeEach
    void setUp() {
        validator = new UniquenessValidator(customerRepositoryPort);
        testCustomerDetails = CustomerDetails.builder()
                .firstName("John")
                .familyName("Doe")
                .email("john.doe@example.com")
                .birthDate(Instant.parse("1990-01-15T10:00:00Z"))
                .build();
    }

    @Test
    void whenCustomerIsUnique_shouldHaveNoErrors() {
        when(customerRepositoryPort.findByEmail(testCustomerDetails.getEmail())).thenReturn(Optional.empty());
        when(customerRepositoryPort.existsByFirstNameAndFamilyNameAndBirthDate(
                testCustomerDetails.getFirstName(),
                testCustomerDetails.getFamilyName(),
                testCustomerDetails.getBirthDate()
        )).thenReturn(false);

        ValidationResult result = new ValidationResult();
        validator.validate(testCustomerDetails, result);

        assertThat(result.hasErrors()).isFalse();
    }

    @Test
    void whenEmailExists_shouldHaveError() {
        when(customerRepositoryPort.findByEmail(testCustomerDetails.getEmail())).thenReturn(Optional.of(Customer.builder().build()));
        when(customerRepositoryPort.existsByFirstNameAndFamilyNameAndBirthDate(
                testCustomerDetails.getFirstName(),
                testCustomerDetails.getFamilyName(),
                testCustomerDetails.getBirthDate()
        )).thenReturn(false);

        ValidationResult result = new ValidationResult();
        validator.validate(testCustomerDetails, result);

        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getErrors()).contains("User with this email already exists. Please choose a unused email");
    }

    @Test
    void whenNameAndBirthDateExists_shouldHaveError() {
        when(customerRepositoryPort.findByEmail(testCustomerDetails.getEmail())).thenReturn(Optional.empty());
        when(customerRepositoryPort.existsByFirstNameAndFamilyNameAndBirthDate(
                testCustomerDetails.getFirstName(),
                testCustomerDetails.getFamilyName(),
                testCustomerDetails.getBirthDate()
        )).thenReturn(true);

        ValidationResult result = new ValidationResult();
        validator.validate(testCustomerDetails, result);

        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getErrors()).contains("A user with the same first name, last name, and birth date already exists.");
    }

    @Test
    void whenBothEmailAndNameAndBirthDateExist_shouldHaveTwoErrors() {
        when(customerRepositoryPort.findByEmail(testCustomerDetails.getEmail())).thenReturn(Optional.of(Customer.builder().build()));
        when(customerRepositoryPort.existsByFirstNameAndFamilyNameAndBirthDate(
                testCustomerDetails.getFirstName(),
                testCustomerDetails.getFamilyName(),
                testCustomerDetails.getBirthDate()
        )).thenReturn(true);

        ValidationResult result = new ValidationResult();
        validator.validate(testCustomerDetails, result);

        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getErrors()).hasSize(2);
        assertThat(result.getErrors()).contains(
                "User with this email already exists. Please choose a unused email",
                "A user with the same first name, last name, and birth date already exists."
        );
    }
}
