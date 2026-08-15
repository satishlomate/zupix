package io.zupix;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Generates a compact OpenAPI document from registered routes. */
public final class OpenApiGenerator {
    public OpenAPI generate(Router router) {
        Map<String, List<OpenAPI.Operation>> grouped = new LinkedHashMap<>();
        for (Route route : router.routes()) {
            grouped.computeIfAbsent(route.path(), ignored -> new ArrayList<>())
                    .add(new OpenAPI.Operation(route.method().toLowerCase(), "Zupix API operation"));
        }
        var items = grouped.entrySet().stream()
                .map(entry -> new OpenAPI.PathItem(entry.getKey(), List.copyOf(entry.getValue())))
                .toList();
        return new OpenAPI("3.0.3", new OpenAPI.Info("Zupix API", "0.1.0"), new OpenAPI.Paths(items));
    }
}
