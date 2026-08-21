package io.zupix;

import java.util.Set;
import java.util.stream.Collectors;

/** Configurable CORS policy used by the HTTP runtime. */
public final class CorsMiddleware implements Middleware {
    private final String allowOrigin;
    private final Set<String> methods;
    private final Set<String> headers;

    public CorsMiddleware() {
        this("*", Set.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"), Set.of("Content-Type", "Authorization"));
    }

    public CorsMiddleware(String allowOrigin, Set<String> methods, Set<String> headers) {
        if (allowOrigin == null || allowOrigin.isBlank()) throw new IllegalArgumentException("allowOrigin must not be blank");
        this.allowOrigin = allowOrigin;
        this.methods = Set.copyOf(methods);
        this.headers = Set.copyOf(headers);
    }

    public String allowOrigin() { return allowOrigin; }
    public Set<String> methods() { return methods; }
    public Set<String> headers() { return headers; }
    public String allowMethodsHeader() { return methods.stream().sorted().collect(Collectors.joining(", ")); }
    public String allowHeadersHeader() { return headers.stream().sorted(String.CASE_INSENSITIVE_ORDER).collect(Collectors.joining(", ")); }

    @Override public void handle(RequestContext request, MiddlewareChain chain) { chain.next(); }
}
