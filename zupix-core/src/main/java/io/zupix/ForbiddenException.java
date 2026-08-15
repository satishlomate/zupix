package io.zupix;

/** Signals that an authenticated caller lacks permission for a resource. */
public final class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) { super(message); }
}
