package io.zupix;

import java.util.LinkedHashMap;
import java.util.Map;

/** Explicit HTTP response returned by a Zupix handler. */
public record Response(int status, Object body, Map<String, String> headers) {
    public Response {
        if (status < 100 || status > 599) throw new IllegalArgumentException("Invalid HTTP status: " + status);
        headers = Map.copyOf(headers == null ? Map.of() : headers);
    }

    public static Response ok(Object body) { return status(200, body); }
    public static Response created(Object body) { return status(201, body); }
    public static Response noContent() { return new Response(204, null, Map.of()); }
    public static Response status(int status, Object body) { return new Response(status, body, Map.of()); }

    public Response header(String name, String value) {
        Map<String, String> copy = new LinkedHashMap<>(headers);
        copy.put(name, value);
        return new Response(status, body, copy);
    }
}
