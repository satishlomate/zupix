package io.zupix.security;

import io.zupix.Middleware;
import io.zupix.MiddlewareChain;
import io.zupix.RequestContext;

import java.util.Objects;

/** Middleware that authenticates requests and exposes authentication to downstream handlers. */
public final class SecurityMiddleware implements Middleware {
    private final Authenticator authenticator;

    public SecurityMiddleware(Authenticator authenticator) {
        this.authenticator = Objects.requireNonNull(authenticator, "authenticator");
    }

    public Authentication authenticate(RequestContext request) {
        Authentication authentication = authenticator.authenticate(request);
        return authentication == null ? Authentication.anonymous() : authentication;
    }

    @Override
    public void handle(RequestContext request, MiddlewareChain chain) {
        AuthenticationContext.set(authenticate(request));
        try { chain.next(); }
        finally { AuthenticationContext.clear(); }
    }
}
