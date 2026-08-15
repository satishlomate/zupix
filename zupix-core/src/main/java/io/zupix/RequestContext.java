package io.zupix;

/** Request data exposed to the Zupix invocation layer. */
public record RequestContext(String method, String path, String query, String body) {
}
