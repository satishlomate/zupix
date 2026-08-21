package io.zupix;

/** Requires an authenticated principal with a configured role. */
public final class AuthorizationMiddleware implements Middleware {
    private final String requiredRole;

    public AuthorizationMiddleware(String requiredRole) {
        if (requiredRole == null || requiredRole.isBlank()) throw new IllegalArgumentException("requiredRole must not be blank");
        this.requiredRole = requiredRole;
    }

    @Override
    public void handle(RequestContext request, MiddlewareChain chain) {
        Principal principal = SecurityContext.current();
        if (principal == null) throw new UnauthorizedException("Authentication required");
        if (!principal.hasRole(requiredRole)) throw new ForbiddenException("Forbidden");
        chain.next();
    }
}
