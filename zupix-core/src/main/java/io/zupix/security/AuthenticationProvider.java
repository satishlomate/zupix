package io.zupix.security;

import io.zupix.RequestContext;

/** Resolves authentication for an incoming request. */
@FunctionalInterface
public interface AuthenticationProvider {
    Authentication authenticate(RequestContext request);
}
