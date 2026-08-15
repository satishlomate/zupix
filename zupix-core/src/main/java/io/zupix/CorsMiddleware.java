package io.zupix;

import java.util.Set;

/** Simple configurable CORS middleware for browser clients. */
public final class CorsMiddleware implements Middleware {
    private final String allowOrigin;
    private final Set<String> methods;
    private final Set<String> headers;

    public CorsMiddleware() {
        this("*", Set.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"), Set.of("Content-Type", "Authorization"));
    }

    public CorsMiddleware(String allowOrigin, Set<String> methods, Set<String> headers) {
        this.allowOrigin = allowOrigin;
        this.methods = Set.copyOf(methods);
        this.headers = Set.copyOf(headers);
    }

    public String allowOrigin() { return allowOrigin; }
    public Set<String> methods() { return methods; }
    public Set<String> headers() { return headers; }

    @Override
    public void handle(RequestContext request, MiddlewareChain chain) {
        chain.next();
    }
}
