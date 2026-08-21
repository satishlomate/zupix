package io.zupix.test;

import io.zupix.Response;

/** Lightweight assertions-friendly response wrapper for Zupix tests. */
public record TestResponse(int status, Object body, java.util.Map<String, String> headers) {
    public static TestResponse from(Response response) {
        if (response == null) throw new IllegalArgumentException("response must not be null");
        return new TestResponse(response.status(), response.body(), response.headers());
    }

    public String header(String name) {
        return headers.entrySet().stream()
                .filter(e -> e.getKey().equalsIgnoreCase(name))
                .map(java.util.Map.Entry::getValue)
                .findFirst().orElse(null);
    }
}
