package io.zupix.security;

/** Verifies a bearer token and returns the authenticated principal. */
@FunctionalInterface
public interface TokenVerifier {
    Authentication verify(String token);
}
