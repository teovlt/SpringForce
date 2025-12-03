package fr.imt.springforce.customer.domain.policies;

import fr.imt.springforce.common.validation.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LicenceValidatorTest {

    private LicenceValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new LicenceValidator();
    }

    @Test
    public void testValidLicenceNumber() {
        String validLicenceNumber = "12012A123456";
        ValidationResult result = new ValidationResult();
        validator.validate(validLicenceNumber, result);
        assertFalse(result.hasErrors());
    }

    @Test
    public void testInvalidLicenceNumber() {
        String invalidLicenceNumber = "123456789";
        ValidationResult result = new ValidationResult();
        validator.validate(invalidLicenceNumber, result);
        assertTrue(result.hasErrors());
        assertEquals(1, result.getErrors().size());
        assertTrue(result.getErrors().contains("Invalid licence number format"));
    }

    @Test
    public void testNullLicenceNumber() {
        String nullLicenceNumber = null;
        ValidationResult result = new ValidationResult();
        validator.validate(nullLicenceNumber, result);
        assertTrue(result.hasErrors());
        assertEquals(1, result.getErrors().size());
        assertTrue(result.getErrors().contains("Invalid licence number format"));
    }
}
