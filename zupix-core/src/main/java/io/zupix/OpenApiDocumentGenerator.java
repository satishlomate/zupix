package io.zupix;

import java.util.LinkedHashMap;
import java.util.Map;

/** Generates a JSON-serializable OpenAPI 3.0 document from registered routes. */
public final class OpenApiDocumentGenerator {
    public Map<String, Object> generate(Router router) {
        Map<String, Object> paths = new LinkedHashMap<>();
        for (Route route : router.routes()) {
            @SuppressWarnings("unchecked")
            Map<String, Object> operations = (Map<String, Object>) paths.computeIfAbsent(
                    route.path(), ignored -> new LinkedHashMap<String, Object>());
            Map<String, Object> operation = new LinkedHashMap<>();
            operation.put("summary", "Zupix API operation");
            operation.put("responses", Map.of("200", Map.of("description", "Successful response")));
            operations.put(route.method().toLowerCase(), operation);
        }
        return Map.of(
                "openapi", "3.0.3",
                "info", Map.of("title", "Zupix API", "version", "0.1.0"),
                "paths", paths
        );
    }
}
