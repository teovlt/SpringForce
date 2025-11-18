package fr.imt.springforce.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;

/**
 * This @RestControllerAdvice intercepts all responses from @RestController
 * classes.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<HttpResponse<Void>> handleResourceNotFound(ResourceNotFoundException ex) {
        HttpResponse<Void> errorResponse = HttpResponse.error(
                "Resource Not Found",
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<HttpResponse<Void>> handleNoResourceFoundException(NoResourceFoundException ex) {
        log.error("No resource found exception: ", ex);
        HttpResponse<Void> errorResponse = HttpResponse.error("Resource Not Found");
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpResponse<Void>> handleValidationErrors(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.error("Validation error: {}", errorMessage);
        HttpResponse<Void> errorResponse = HttpResponse.error(
                "Validation Failed",
                errorMessage
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<HttpResponse<Void>> handleWrongHttpVerb(HttpRequestMethodNotSupportedException ex) {
        log.error("Wrong HTTP verb: {}", ex.getMessage());
        HttpResponse<Void> errorResponse = HttpResponse.error(
                "Method Not Allowed",
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<HttpResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        log.error("Illegal argument exception: ", ex);
        HttpResponse<Void> errorResponse = HttpResponse.error(
                "Invalid Request",
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse<Void>> handleGenericException(Exception ex) {
        log.error("Unhandled exception: ", ex);
        HttpResponse<Void> errorResponse = HttpResponse.error(
                "An internal server error occurred",
                "Please contact support."
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}