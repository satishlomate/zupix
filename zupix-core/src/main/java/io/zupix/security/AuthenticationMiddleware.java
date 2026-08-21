package io.zupix.security;

import io.zupix.Middleware;
import io.zupix.MiddlewareChain;
import io.zupix.RequestContext;

import java.util.Objects;

/** Authenticates a request and makes its principal available to route authorization. */
public final class AuthenticationMiddleware implements Middleware {
    private final Authenticator authenticator;

    public AuthenticationMiddleware(Authenticator authenticator) {
        this.authenticator = Objects.requireNonNull(authenticator, "authenticator");
    }

    @Override
    public void handle(RequestContext request, MiddlewareChain chain) {
        Authentication authentication = authenticator.authenticate(request);
        AuthenticationContext.set(authentication == null ? Authentication.anonymous() : authentication);
        try { chain.next(); }
        finally { AuthenticationContext.clear(); }
    }
}
