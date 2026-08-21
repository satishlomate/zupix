package io.zupix.security;

import io.zupix.RequestContext;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthenticationMiddlewareTest {
    private final RequestContext request = new RequestContext("GET", "/", null, Map.of(), "");

    @Test
    void exposesAuthenticationOnlyDuringChain() {
        Authentication authentication = Authentication.authenticated("satish");
        new AuthenticationMiddleware(req -> authentication).handle(request, () -> {
            assertTrue(AuthenticationContext.current().authenticated());
            assertEquals("satish", AuthenticationContext.current().principal());
        });
        assertFalse(AuthenticationContext.current().authenticated());
    }
}
