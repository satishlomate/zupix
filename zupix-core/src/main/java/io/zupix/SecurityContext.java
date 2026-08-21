package io.zupix;

/** Request-scoped security context backed by a virtual-thread-local value. */
public final class SecurityContext {
    private static final ThreadLocal<Principal> CURRENT = new ThreadLocal<>();

    private SecurityContext() {}

    public static Principal current() { return CURRENT.get(); }
    public static void set(Principal principal) { CURRENT.set(principal); }
    public static void clear() { CURRENT.remove(); }
}
