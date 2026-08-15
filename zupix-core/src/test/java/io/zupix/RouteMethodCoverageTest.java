package io.zupix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RouteMethodCoverageTest {
    @Test
    void discoversAllHttpMethods() {
        var routes = new RouteScanner().scan(new Api());
        assertEquals(5, routes.size());
        assertEquals(java.util.Set.of("GET", "POST", "PUT", "PATCH", "DELETE"),
                routes.stream().map(r -> r.route().method()).collect(java.util.stream.Collectors.toSet()));
    }

    static class Api {
        @Get("/a") String get() { return "get"; }
        @Post("/a") String post() { return "post"; }
        @Put("/a") String put() { return "put"; }
        @Patch("/a") String patch() { return "patch"; }
        @Delete("/a") String delete() { return "delete"; }
    }
}
