package io.zupix.security;

/** Represents the authenticated principal for a request. */
public record Authentication(Object principal, boolean authenticated) {
    public static Authentication anonymous() { return new Authentication(null, false); }
    public static Authentication authenticated(Object principal) { return new Authentication(principal, true); }
}
