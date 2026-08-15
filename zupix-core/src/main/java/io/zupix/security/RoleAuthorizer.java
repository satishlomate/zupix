package io.zupix.security;

import java.util.Arrays;
import java.util.Collection;

/** Default role authorizer for principals exposing a role collection. */
public final class RoleAuthorizer implements Authorizer {
    @Override
    public boolean allowed(Authentication authentication, String[] requiredRoles) {
        if (!authentication.authenticated()) return false;
        if (requiredRoles.length == 0) return true;
        if (!(authentication.principal() instanceof Collection<?> roles)) return false;
        return Arrays.stream(requiredRoles).anyMatch(role -> roles.contains(role));
    }
}
