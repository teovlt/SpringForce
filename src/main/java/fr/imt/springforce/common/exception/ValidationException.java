package fr.imt.springforce.common.exception;

import java.util.List;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends RuntimeException {
    private final List<String> errors;

    public ValidationException(List<String> errors) {
        super(String.join(", ", errors));
        this.errors = errors;
    }

    public ValidationException(String message) {
        super(message);
        this.errors = List.of(message);
    }
}
