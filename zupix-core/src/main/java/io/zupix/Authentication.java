package io.zupix;

/** Resolves the authenticated principal for an HTTP request. */
@FunctionalInterface
public interface Authentication {
    Principal authenticate(RequestContext request);
}
