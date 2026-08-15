package io.zupix;

/** Signals that a request has no valid authenticated principal. */
public final class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) { super(message); }
}
