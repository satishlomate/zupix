package io.zupix;

/** Intercepts an HTTP request before and/or after route handling. */
@FunctionalInterface
public interface Middleware {
    void handle(RequestContext request, MiddlewareChain chain);
}
