package fr.imt.springforce.common.validation;

import fr.imt.springforce.common.exception.ValidationException;

import java.util.Arrays;
import java.util.List;

public class ValidationChain<T> {

    private final List<Validator<T>> validators;

    private ValidationChain(List<Validator<T>> validators) {
        this.validators = validators;
    }

    @SafeVarargs
    public static <T> ValidationChain<T> of(Validator<T>... validators) {
        return new ValidationChain<>(Arrays.asList(validators));
    }

    public void validate(T toValidate) {
        ValidationResult result = new ValidationResult();
        for (Validator<T> validator : validators) {
            validator.validate(toValidate, result);
        }

        if (result.hasErrors()) {
            throw new ValidationException(result.getErrors());
        }
    }
}
