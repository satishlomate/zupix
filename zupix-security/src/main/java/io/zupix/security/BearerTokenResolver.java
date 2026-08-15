package io.zupix.security;

import io.zupix.RequestContext;

/** Extracts a bearer token from the HTTP Authorization header. */
@FunctionalInterface
public interface BearerTokenResolver {
    String resolve(RequestContext request);

    static BearerTokenResolver authorizationHeader() {
        return request -> {
            String value = request.header("Authorization");
            if (value == null || value.isBlank()) return null;
            if (!value.regionMatches(true, 0, "Bearer ", 0, 7)) return null;
            String token = value.substring(7).trim();
            return token.isEmpty() ? null : token;
        };
    }
}
