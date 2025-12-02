package fr.imt.springforce.customer.domain.policies;

import java.util.Collections;
import java.util.Set;
import java.util.regex.Pattern;

public class LicenceValidator {

    private static final Pattern LICENCE_PATTERN = Pattern.compile(
            "\\b[0-9]{2}(?:0[1-9]|1[0-2])(?:0[1-9]|1[0-9]|2A|2a|2B|2b|2[1-9]|[3-8][0-9]|9[0-5])[0-9]{6}\\b"
    );

    public static Set<String> validate(String licenceNumber) {
        if (licenceNumber == null || !LICENCE_PATTERN.matcher(licenceNumber).matches()) {
            return Set.of("Invalid licence number format");
        }
        return Collections.emptySet();
    }
}