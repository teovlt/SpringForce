package fr.imt.springforce.customer.domain.policies;

import fr.imt.springforce.common.validation.ValidationResult;
import fr.imt.springforce.customer.domain.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AddressValidatorTest {

    private AddressValidator validator;

    @BeforeEach
    void setUp() {
        validator = new AddressValidator();
    }

    @Test
    void whenAddressIsValid_shouldReturnNoErrors() {
        Address address = Address.builder()
                .streetNumber("123")
                .streetName("Main Street")
                .postalCode("75000")
                .city("Paris")
                .countryCode("FR")
                .build();
        ValidationResult result = new ValidationResult();
        validator.validate(address, result);
        assertThat(result.hasErrors()).isFalse();
    }

    @Test
    void whenCountryCodeIsNull_shouldReturnError() {
        Address address = Address.builder()
                .streetNumber("123")
                .streetName("Main Street")
                .postalCode("75000")
                .city("Paris")
                .countryCode(null)
                .build();
        ValidationResult result = new ValidationResult();
        validator.validate(address, result);
        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getErrors()).contains("Country code is required (ISO 3166-1 alpha-2).");
    }

    @Test
    void whenCountryCodeIsInvalid_shouldReturnError() {
        Address address = Address.builder()
                .streetNumber("123")
                .streetName("Main Street")
                .postalCode("75000")
                .city("Paris")
                .countryCode("XX")
                .build();
        ValidationResult result = new ValidationResult();
        validator.validate(address, result);
        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getErrors()).contains("Invalid country code. Use ISO 3166-1 alpha-2 (e.g., 'US', 'FR').");
    }

    @Test
    void whenCityIsNull_shouldReturnError() {
        Address address = Address.builder()
                .streetNumber("123")
                .streetName("Main Street")
                .postalCode("75000")
                .city(null)
                .countryCode("FR")
                .build();
        ValidationResult result = new ValidationResult();
        validator.validate(address, result);
        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getErrors()).contains("City is required.");
    }

    @Test
    void whenPostalCodeIsInvalid_shouldReturnError() {
        Address address = Address.builder()
                .streetNumber("123")
                .streetName("Main Street")
                .postalCode("12345678901")
                .city("Paris")
                .countryCode("FR")
                .build();
        ValidationResult result = new ValidationResult();
        validator.validate(address, result);
        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getErrors()).contains("Postal code format is invalid (e.g., '75000' or '90210').");
    }

    @Test
    void whenStreetNumberIsInvalid_shouldReturnError() {
        Address address = Address.builder()
                .streetNumber("12345678901")
                .streetName("Main Street")
                .postalCode("75000")
                .city("Paris")
                .countryCode("FR")
                .build();
        ValidationResult result = new ValidationResult();
        validator.validate(address, result);
        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getErrors()).contains("Street number format is invalid (e.g., '123', '12A', '12/3').");
    }

    @Test
    void whenMultipleFieldsAreInvalid_shouldReturnMultipleErrors() {
        Address address = Address.builder()
                .streetNumber("12345678901")
                .streetName("Main Street")
                .postalCode("12345678901")
                .city(null)
                .countryCode("XX")
                .build();
        ValidationResult result = new ValidationResult();
        validator.validate(address, result);
        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getErrors()).hasSize(4);
        assertThat(result.getErrors()).contains(
                "Invalid country code. Use ISO 3166-1 alpha-2 (e.g., 'US', 'FR').",
                "City is required.",
                "Postal code format is invalid (e.g., '75000' or '90210').",
                "Street number format is invalid (e.g., '123', '12A', '12/3')."
        );
    }
}
