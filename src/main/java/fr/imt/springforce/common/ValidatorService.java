package fr.imt.springforce.common;

public interface ValidatorService<T> {

    ValidationResult validate(T validate);

}
