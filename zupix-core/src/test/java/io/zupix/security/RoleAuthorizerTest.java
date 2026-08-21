package io.zupix.security;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RoleAuthorizerTest {
    private final RoleAuthorizer authorizer = new RoleAuthorizer();

    @Test
    void deniesAnonymous() {
        assertFalse(authorizer.allowed(Authentication.anonymous(), new String[]{"admin"}));
    }

    @Test
    void allowsMatchingRole() {
        assertTrue(authorizer.allowed(Authentication.authenticated(List.of("user", "admin")), new String[]{"admin"}));
    }

    @Test
    void rejectsNonMatchingRole() {
        assertFalse(authorizer.allowed(Authentication.authenticated(List.of("user")), new String[]{"admin"}));
    }
}
