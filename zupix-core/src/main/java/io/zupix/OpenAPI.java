package io.zupix;

import java.util.List;
import java.util.Map;

/** Minimal OpenAPI 3.0 document model for generated API metadata. */
public record OpenAPI(String openapi, Info info, Paths paths) {
    public record Info(String title, String version) {}
    public record Paths(Map<String, PathItem> items) {}
    public record PathItem(Map<String, Operation> operations) {}
    public record Operation(String method, String summary, List<Parameter> parameters, RequestBody requestBody) {}
    public record Parameter(String name, String in, boolean required, Schema schema) {}
    public record RequestBody(boolean required, Schema schema) {}
    public record Schema(String type, Map<String, Object> properties) {}
}
