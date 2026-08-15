package io.zupix.security;

import io.zupix.Middleware;
import io.zupix.MiddlewareChain;
import io.zupix.RequestContext;

/** Authenticates a request and makes its principal available to route authorization. */
public final class AuthenticationMiddleware implements Middleware {
    private final Authenticator authenticator;
    public AuthenticationMiddleware(Authenticator authenticator) { this.authenticator = authenticator; }

    @Override
    public void handle(RequestContext request, MiddlewareChain chain) {
        AuthenticationContext.set(authenticator.authenticate(request));
        try { chain.next(); }
        finally { AuthenticationContext.clear(); }
    }
}
