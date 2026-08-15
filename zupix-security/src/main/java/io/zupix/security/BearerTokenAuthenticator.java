package io.zupix.security;

import io.zupix.RequestContext;
import java.util.Objects;
import java.util.function.Function;

/**
 * Lightweight bearer-token adapter. Token verification is delegated to the supplied function;
 * cryptographic JWT verification remains outside the core module.
 */
public final class BearerTokenAuthenticator implements Authenticator {
    private final Function<String, Authentication> verifier;

    public BearerTokenAuthenticator(Function<String, Authentication> verifier) {
        this.verifier = Objects.requireNonNull(verifier, "verifier");
    }

    @Override
    public Authentication authenticate(RequestContext request) {
        String header = requestHeader(request, "Authorization");
        if (header == null || !header.regionMatches(true, 0, "Bearer ", 0, 7)) {
            return Authentication.anonymous();
        }
        String token = header.substring(7).trim();
        return token.isEmpty() ? Authentication.anonymous() : verifier.apply(token);
    }

    private static String requestHeader(RequestContext request, String name) {
        // Headers are intentionally not stored in the current RequestContext yet.
        // This adapter is ready for the header-aware RequestContext milestone.
        return null;
    }
}
