package fr.imt.springforce.customer.domain.policies;

import fr.imt.springforce.common.validation.ValidationResult;
import fr.imt.springforce.common.validation.Validator;

import java.util.regex.Pattern;

public class LicenceValidator implements Validator<String> {

    private static final Pattern LICENCE_PATTERN = Pattern.compile(
            "\\b[0-9]{2}(?:0[1-9]|1[0-2])(?:0[1-9]|1[0-9]|2A|2a|2B|2b|2[1-9]|[3-8][0-9]|9[0-5])[0-9]{6}\\b"
    );

    @Override
    public void validate(String licenceNumber, ValidationResult result) {
        if (licenceNumber == null || !LICENCE_PATTERN.matcher(licenceNumber).matches()) {
            result.addError("Invalid licence number format");
        }
    }
}