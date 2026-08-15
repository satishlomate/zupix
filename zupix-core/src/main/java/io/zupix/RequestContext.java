package io.zupix;

import java.util.Map;

/** Immutable HTTP request data exposed to the Zupix invocation layer. */
public record RequestContext(String method, String path, String query, Map<String, String> headers, String body) {
    public RequestContext {
        headers = headers == null ? Map.of() : Map.copyOf(headers);
    }
    public String header(String name) {
        return headers.entrySet().stream().filter(e -> e.getKey().equalsIgnoreCase(name)).map(Map.Entry::getValue).findFirst().orElse(null);
    }
    public RequestContext(String method, String path, String query, String body) {
        this(method, path, query, Map.of(), body);
    }
}
