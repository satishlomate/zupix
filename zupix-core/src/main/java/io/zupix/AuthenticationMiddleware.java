package io.zupix;

/** Middleware that authenticates a request and exposes the principal to downstream code. */
public final class AuthenticationMiddleware implements Middleware {
    private final Authentication authentication;

    public AuthenticationMiddleware(Authentication authentication) {
        if (authentication == null) throw new IllegalArgumentException("authentication must not be null");
        this.authentication = authentication;
    }

    @Override
    public void handle(RequestContext request, MiddlewareChain chain) {
        Principal principal = authentication.authenticate(request);
        if (principal == null) throw new UnauthorizedException("Authentication required");
        chain.next();
    }
}
