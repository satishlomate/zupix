package io.zupix;

import java.util.Set;

/** Authenticated application principal. */
public record Principal(String name, Set<String> roles) {
    public Principal {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name must not be blank");
        roles = Set.copyOf(roles == null ? Set.of() : roles);
    }

    public boolean hasRole(String role) {
        return role != null && roles.contains(role);
    }
}
