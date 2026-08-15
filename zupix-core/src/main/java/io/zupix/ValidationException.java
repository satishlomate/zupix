package io.zupix;

import java.util.List;

/** Raised when a validated request model violates its constraints. */
public final class ValidationException extends RuntimeException {
    private final List<String> errors;

    public ValidationException(List<String> errors) {
        super("Request validation failed");
        this.errors = List.copyOf(errors);
    }

    public List<String> errors() { return errors; }
}
