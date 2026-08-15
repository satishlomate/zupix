package io.zupix;

import java.util.List;

/** Minimal OpenAPI 3.0 document model for generated API metadata. */
public record OpenAPI(String openapi, Info info, Paths paths) {
    public record Info(String title, String version) {}
    public record Paths(List<PathItem> items) {}
    public record PathItem(String path, List<Operation> operations) {}
    public record Operation(String method, String summary) {}
}
