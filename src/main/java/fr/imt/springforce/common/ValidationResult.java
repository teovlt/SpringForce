package fr.imt.springforce.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ValidationResult {

    boolean isValid;
    String infoType;
    String errorMsg;

    public static ValidationResult valid() {
        return new ValidationResult(true, "VALID", null);
    }

    public static ValidationResult invalid(String errorMsg, String infoType) {
        return new ValidationResult(false, infoType, errorMsg);
    }

    public boolean isNotValid() {
        return !isValid;
    }

}
