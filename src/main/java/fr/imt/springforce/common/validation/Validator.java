package fr.imt.springforce.common.validation;

@FunctionalInterface
public interface Validator<T> {
    void validate(T toValidate, ValidationResult result);
}
