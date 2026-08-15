package io.zupix.security;

/** Request-scoped authentication holder used by the Zupix runtime. */
public final class AuthenticationContext {
    private static final ThreadLocal<Authentication> CURRENT = ThreadLocal.withInitial(Authentication::anonymous);
    private AuthenticationContext() {}
    public static Authentication current() { return CURRENT.get(); }
    public static void set(Authentication authentication) { CURRENT.set(authentication); }
    public static void clear() { CURRENT.remove(); }
}
