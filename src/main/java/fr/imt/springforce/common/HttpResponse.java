package fr.imt.springforce.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpResponse<T> {

    private final Instant timestamp;
    private final String message;
    private final T data;
    private final List<String> errors;

    // Private constructor, use static factory methods
    private HttpResponse(String message, T data, List<String> errors) {
        this.timestamp = Instant.now();
        this.message = message;
        this.data = data;
        this.errors = errors;
    }

    public static <T> HttpResponse<T> success(T data) {
        return new HttpResponse<>("Success", data, null);
    }

    public static HttpResponse<Void> success() {
        return new HttpResponse<>("Success", null, null);
    }

    public static HttpResponse<Void> error(String message, String error) {
        return new HttpResponse<>(message, null, List.of(error));
    }

    public static HttpResponse<Void> error(String message) {
        return new HttpResponse<>(message, null, Collections.emptyList());
    }

}
