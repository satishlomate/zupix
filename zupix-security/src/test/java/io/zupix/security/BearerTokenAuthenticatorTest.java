package io.zupix.security;

import io.zupix.RequestContext;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class BearerTokenAuthenticatorTest {
    @Test
    void extractsBearerTokenFromAuthorizationHeader() {
        var authenticator = new BearerTokenAuthenticator(token -> Authentication.authenticated(token));
        var request = new RequestContext("GET", "/", null, Map.of("Authorization", "Bearer abc123"), "");
        var authentication = authenticator.authenticate(request);
        assertTrue(authentication.authenticated());
        assertEquals("abc123", authentication.principal());
    }

    @Test
    void missingBearerTokenIsAnonymous() {
        var authenticator = new BearerTokenAuthenticator(token -> Authentication.authenticated(token));
        var authentication = authenticator.authenticate(new RequestContext("GET", "/", null, Map.of(), ""));
        assertFalse(authentication.authenticated());
    }
}
