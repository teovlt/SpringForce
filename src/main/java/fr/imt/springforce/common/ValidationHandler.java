package fr.imt.springforce.common;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.internal.engine.validationcontext.ValidationContext;

@Getter
@Setter
public abstract class ValidationHandler<T> extends Handler<T> {

    public final ValidationResult validatesAll(ValidationContext ctx) {
        ValidationResult result = ValidationResult.valid();
        ValidationHandler<T> current = (ValidationHandler<T>)getFirst();
        while(current != null) {
            result = current.validate(ctx);
            if(!result.isValid()) {
                return  result;
            }
            current = (ValidationHandler<T>)current.getNext();
        }
        return result;
    }

    public abstract ValidationResult validate(ValidationContext ctx);

}
