package io.zupix.security;

import io.zupix.Middleware;
import io.zupix.RequestContext;

import java.util.Objects;

/** Middleware that authenticates requests using a supplied strategy. */
public final class SecurityMiddleware implements Middleware {
    private final Authenticator authenticator;

    public SecurityMiddleware(Authenticator authenticator) {
        this.authenticator = Objects.requireNonNull(authenticator, "authenticator");
    }

    public Authentication authenticate(RequestContext request) {
        return authenticator.authenticate(request);
    }

    @Override
    public void handle(RequestContext request, io.zupix.MiddlewareChain chain) {
        authenticator.authenticate(request);
        chain.next();
    }
}
