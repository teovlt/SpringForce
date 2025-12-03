package fr.imt.springforce.customer.domain.policies;

import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LicenceValidatorTest {

    @Test
    public void testValidLicenceNumber() {
        String validLicenceNumber = "12012A123456";
        Set<String> errors = LicenceValidator.validate(validLicenceNumber);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testInvalidLicenceNumber() {
        String invalidLicenceNumber = "123456789";
        Set<String> errors = LicenceValidator.validate(invalidLicenceNumber);
        assertEquals(1, errors.size());
        assertTrue(errors.contains("Invalid licence number format"));
    }

    @Test
    public void testNullLicenceNumber() {
        String nullLicenceNumber = null;
        Set<String> errors = LicenceValidator.validate(nullLicenceNumber);
        assertEquals(1, errors.size());
        assertTrue(errors.contains("Invalid licence number format"));
    }
}
