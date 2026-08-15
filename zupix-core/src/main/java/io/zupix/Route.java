package io.zupix;

import java.util.Objects;

/** Immutable description of an HTTP route. */
public record Route(String method, String path) {

    public Route {
        Objects.requireNonNull(method, "method must not be null");
        Objects.requireNonNull(path, "path must not be null");
        if (method.isBlank()) {
            throw new IllegalArgumentException("method must not be blank");
        }
        if (path.isBlank() || !path.startsWith("/")) {
            throw new IllegalArgumentException("path must start with '/'");
        }
    }
}
