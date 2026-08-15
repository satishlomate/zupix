package io.zupix;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MiddlewarePipelineTest {
    @Test
    void executesMiddlewareInOrder() {
        var events = new ArrayList<String>();
        var registry = new MiddlewareRegistry()
                .use((request, next) -> { events.add("before-1"); next.next(); events.add("after-1"); })
                .use((request, next) -> { events.add("before-2"); next.next(); events.add("after-2"); });

        new MiddlewarePipeline(registry.all()).execute(
                new RequestContext("GET", "/", null, ""),
                () -> events.add("endpoint"));

        assertEquals(java.util.List.of("before-1", "before-2", "endpoint", "after-2", "after-1"), events);
    }
}
