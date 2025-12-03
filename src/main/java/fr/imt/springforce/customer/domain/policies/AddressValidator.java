package fr.imt.springforce.customer.domain.policies;

import fr.imt.springforce.common.validation.ValidationResult;
import fr.imt.springforce.common.validation.Validator;
import fr.imt.springforce.customer.domain.Address;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Validates postal address
 */
public class AddressValidator implements Validator<Address> {

    private static final Map<String, String> VALID_COUNTRY_CODE = new HashMap<>();

    static {
        VALID_COUNTRY_CODE.put("FR", "France");
    }

    private static final Pattern POSTAL_CODE_REGEX = Pattern.compile("^[A-Za-z0-9\\- ]{3,10}$");
    private static final Pattern STREET_NUMBER_REGEX = Pattern.compile("^[A-Za-z0-9\\-/. ]{1,10}$");
    private static final Pattern COUNTRY_CODE_REGEX = Pattern.compile("^[A-Z]{2}$");

    @Override
    public void validate(Address address, ValidationResult result) {
        // Validate mandatory fields
        validateCountryCode(address, result);

        // Validate field formats
        validatePostalCode(address, result);
        validateStreetNumber(address, result);
        validateCity(address, result);
    }

    /**
     * Validates the country code (ISO 3166-1 alpha-2).
     */
    private void validateCountryCode(Address address, ValidationResult result) {
        if (address.getCountryCode() == null || address.getCountryCode().isEmpty()) {
            result.addError("Country code is required (ISO 3166-1 alpha-2).");
            return;
        }
        if (!isValidCountryCode(address.getCountryCode())) {
            result.addError("Invalid country code. As of now, only addresses with FR code will work");
        }
    }

    /**
     * Validates the city field (non-empty).
     */
    private void validateCity(Address address, ValidationResult result) {
        if (address.getCity() == null || address.getCity().isEmpty()) {
            result.addError("City is required.");
        }
    }

    /**
     * Validates the postal code format (if provided).
     */
    private void validatePostalCode(Address address, ValidationResult result) {
        if (address.getPostalCode() != null && !address.getPostalCode().isEmpty() &&
                !POSTAL_CODE_REGEX.matcher(address.getPostalCode()).matches()) {
            result.addError("Postal code format is invalid (e.g., '75000' or '90210').");
        }
    }

    /**
     * Validates the street number format (if provided).
     */
    private void validateStreetNumber(Address address, ValidationResult result) {
        if (address.getStreetNumber() != null && !address.getStreetNumber().isEmpty() &&
                !STREET_NUMBER_REGEX.matcher(address.getStreetNumber()).matches()) {
            result.addError("Street number format is invalid (e.g., '123', '12A', '12/3').");
        }
    }

    /**
     * Checks if a country code is valid (ISO 3166-1 alpha-2).
     */
    private boolean isValidCountryCode(String countryCode) {
        return countryCode != null && COUNTRY_CODE_REGEX.matcher(countryCode).matches()
                && VALID_COUNTRY_CODE.containsKey(countryCode);
    }
}
