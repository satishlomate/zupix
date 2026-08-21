package io.zupix.test;

import io.zupix.RequestContext;

import java.util.LinkedHashMap;
import java.util.Map;

/** Fluent builder for deterministic unit tests around request handlers and middleware. */
public final class RequestTestBuilder {
    private String method = "GET";
    private String path = "/";
    private String query;
    private String body = "";
    private final Map<String, String> headers = new LinkedHashMap<>();

    public RequestTestBuilder method(String method) { this.method = method; return this; }
    public RequestTestBuilder path(String path) { this.path = path; return this; }
    public RequestTestBuilder query(String query) { this.query = query; return this; }
    public RequestTestBuilder body(String body) { this.body = body; return this; }
    public RequestTestBuilder header(String name, String value) { headers.put(name, value); return this; }

    public RequestContext build() {
        return new RequestContext(method, path, query, headers, body);
    }
}
