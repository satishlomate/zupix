package io.zupix.security;

import io.zupix.RequestContext;
import java.util.Objects;
import java.util.function.Function;

/** Bearer-token adapter. Cryptographic JWT verification is delegated to the supplied verifier. */
public final class BearerTokenAuthenticator implements Authenticator {
    private final Function<String, Authentication> verifier;

    public BearerTokenAuthenticator(Function<String, Authentication> verifier) {
        this.verifier = Objects.requireNonNull(verifier, "verifier");
    }

    @Override
    public Authentication authenticate(RequestContext request) {
        String header = request.header("Authorization");
        if (header == null || !header.regionMatches(true, 0, "Bearer ", 0, 7)) return Authentication.anonymous();
        String token = header.substring(7).trim();
        if (token.isEmpty()) return Authentication.anonymous();
        Authentication result = verifier.apply(token);
        return result == null ? Authentication.anonymous() : result;
    }
}
