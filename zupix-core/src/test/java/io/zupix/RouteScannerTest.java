package io.zupix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RouteScannerTest {

    @Test
    void discoversGetHandler() {
        var routes = new RouteScanner().scan(new ExampleApi());

        assertEquals(1, routes.size());
        assertEquals("GET", routes.getFirst().route().method());
        assertEquals("/hello", routes.getFirst().route().path());
        assertEquals("hello", routes.getFirst().handler().invoke());
    }

    static final class ExampleApi {
        @Get("/hello")
        String hello() {
            return "hello";
        }
    }
}
