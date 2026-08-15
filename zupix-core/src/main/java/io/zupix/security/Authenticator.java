package io.zupix.security;

import io.zupix.RequestContext;

/** Pluggable authentication strategy. */
@FunctionalInterface
public interface Authenticator {
    Authentication authenticate(RequestContext request);
}
