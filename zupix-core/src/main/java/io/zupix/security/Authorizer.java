package io.zupix.security;

/** Pluggable authorization policy. */
@FunctionalInterface
public interface Authorizer {
    boolean allowed(Authentication authentication, String[] requiredRoles);
}
