package io.zupix;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExceptionMiddlewareIntegrationTest {
    @Test
    void middlewareRunsAroundEndpointAndExceptionRegistryMapsErrors() {
        var events = new ArrayList<String>();
        var registry = new MiddlewareRegistry()
                .use((request, next) -> { events.add("before"); next.next(); events.add("after"); });
        var exceptions = new ExceptionRegistry()
                .on(IllegalStateException.class, e -> Response.status(409, e.getMessage()));

        Response[] result = new Response[1];
        new MiddlewarePipeline(registry.all()).execute(
                new RequestContext("GET", "/", null, ""),
                () -> {
                    try { throw new IllegalStateException("conflict"); }
                    catch (RuntimeException error) { result[0] = exceptions.handle(error); }
                });

        assertEquals(java.util.List.of("before", "after"), events);
        assertEquals(409, result[0].status());
        assertEquals("conflict", result[0].body());
    }
}
