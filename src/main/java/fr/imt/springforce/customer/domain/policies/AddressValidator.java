/*
 * -----------------------------------------------------------------
 *  Ce code source est la propriété de Boulanger S.A. Tous droits réservés, 2025.
 *  (C) Copyright Boulanger S.A., 2025
 * -----------------------------------------------------------------
 */
package fr.imt.springforce.customer.domain.policies;

import fr.imt.springforce.customer.domain.Address;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Validates postal address
 */
public class AddressValidator {

    private static final Map<String, String> VALID_COUNTRY_CODE = new HashMap<>();
    static {
        VALID_COUNTRY_CODE.put("FR", "France");
    }

    private static final Pattern POSTAL_CODE_REGEX = Pattern.compile("^[A-Za-z0-9\\- ]{3,10}$");
    private static final Pattern STREET_NUMBER_REGEX = Pattern.compile("^[A-Za-z0-9\\-/. ]{1,10}$");
    private static final Pattern COUNTRY_CODE_REGEX = Pattern.compile("^[A-Z]{2}$");

    /**
     * Validates an Address object.
     * @param address The Address object to validate.
     * @return Set of error messages (empty if valid).
     */
    public static Set<String> validate(Address address) {
        Set<String> errors = new HashSet<>();

        // Validate mandatory fields
        validateCountryCode(address, errors);
        validateCity(address, errors);

        // Validate field formats
        validatePostalCode(address, errors);
        validateStreetNumber(address, errors);

        return errors;
    }

    /**
     * Validates the country code (ISO 3166-1 alpha-2).
     */
    private static void validateCountryCode(Address address, Set<String> errors) {
        if (address.getCountryCode() == null || address.getCountryCode().isEmpty()) {
            errors.add("Country code is required (ISO 3166-1 alpha-2).");
            return;
        }
        if (!isValidCountryCode(address.getCountryCode())) {
            errors.add("Invalid country code. Use ISO 3166-1 alpha-2 (e.g., 'US', 'FR').");
        }
    }

    /**
     * Validates the city field (non-empty).
     */
    private static void validateCity(Address address, Set<String> errors) {
        if (address.getCity() == null || address.getCity().isEmpty()) {
            errors.add("City is required.");
        }
    }

    /**
     * Validates the postal code format (if provided).
     */
    private static void validatePostalCode(Address address, Set<String> errors) {
        if (address.getPostalCode() != null && !address.getPostalCode().isEmpty() &&
                !POSTAL_CODE_REGEX.matcher(address.getPostalCode()).matches()) {
            errors.add("Postal code format is invalid (e.g., '75000' or '90210').");
        }
    }

    /**
     * Validates the street number format (if provided).
     */
    private static void validateStreetNumber(Address address, Set<String> errors) {
        if (address.getStreetNumber() != null && !address.getStreetNumber().isEmpty() &&
                !STREET_NUMBER_REGEX.matcher(address.getStreetNumber()).matches()) {
            errors.add("Street number format is invalid (e.g., '123', '12A', '12/3').");
        }
    }

    /**
     * Checks if a country code is valid (ISO 3166-1 alpha-2).
     */
    private static boolean isValidCountryCode(String countryCode) {
        return countryCode != null && COUNTRY_CODE_REGEX.matcher(countryCode).matches()
                && VALID_COUNTRY_CODE.containsKey(countryCode);
    }

}
