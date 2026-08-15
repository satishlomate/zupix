package io.zupix;

/** Converts application exceptions into HTTP responses. */
@FunctionalInterface
public interface ExceptionHandler<T extends Throwable> {
    Response handle(T exception);
}
