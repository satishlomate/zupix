package io.zupix;

import io.zupix.security.Authentication;
import io.zupix.security.RoleAuthorizer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SecurityAuthorizationIntegrationTest {
    @Test
    void authenticatedRoleIsAllowed() {
        var authorizer = new RoleAuthorizer();
        var authentication = Authentication.authenticated(List.of("USER", "ADMIN"));

        assertTrue(authorizer.allowed(authentication, new String[]{"ADMIN"}));
        assertFalse(authorizer.allowed(authentication, new String[]{"OWNER"}));
    }

    @Test
    void anonymousCallerIsRejected() {
        var authorizer = new RoleAuthorizer();
        assertFalse(authorizer.allowed(Authentication.anonymous(), new String[]{"ADMIN"}));
    }
}
