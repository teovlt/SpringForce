package fr.imt.springforce.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * This @RestControllerAdvice intercepts all responses from @RestController
 * classes.
 */
@Slf4j
@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    /**
     * This method decides whether to intercept the response.
     * We want to intercept everything *except* our own HttpResponse
     * (to avoid infinite loops) and Spring Actuator endpoints.
     */
    @Override
    public boolean supports(MethodParameter returnType,
                            @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        if (returnType.getParameterType().equals(HttpResponse.class)) {
            return false;
        }

        String controllerName = returnType.getContainingClass().getName();
        return !controllerName.contains("org.springframework.boot.actuate");
    }

    /**
     * This is where we wrap the successful response.
     * It's called *after* the controller method returns but *before*
     * the response is written to the client.
     */
    @Override
    public Object beforeBodyWrite(@Nullable Object body,
                                  @NonNull MethodParameter returnType,
                                  @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request,
                                  @NonNull ServerHttpResponse response) {

        if (body == null) {
            // Handle void controller methods
            return HttpResponse.success();
        }

        return HttpResponse.success(body);
    }

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
        HttpResponse<Void> errorResponse = HttpResponse.error("Resource Not Found");
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    /**
     * Handles a specific known exception (e.g., "Not Found").
     */
    @ExceptionHandler(IllegalArgumentException.class) // You can use custom exceptions
    public ResponseEntity<HttpResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        HttpResponse<Void> errorResponse = HttpResponse.error(
                "Invalid Request",
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all other unexpected exceptions as a fallback.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse<Void>> handleGenericException(Exception ex) {
        HttpResponse<Void> errorResponse = HttpResponse.error(
                "An internal server error occurred",
                "Please contact support."
        );

        log.error("Unhandled exception: ", ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}