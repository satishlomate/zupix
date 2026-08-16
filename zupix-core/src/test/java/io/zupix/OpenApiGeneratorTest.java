package io.zupix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OpenApiGeneratorTest {
    @Test
    void generatesOpenApiDocumentForRegisteredRoutes() {
        Router router = new Router();
        router.add(new Route("GET", "/hello", null));
        router.add(new Route("POST", "/users", null));

        OpenAPI document = new OpenApiGenerator().generate(router);

        assertEquals("3.0.3", document.openapi());
        assertEquals("Zupix API", document.info().title());
        assertEquals("0.1.0", document.info().version());
        assertEquals(2, document.paths().items().size());
        assertTrue(document.paths().items().stream().anyMatch(p -> p.path().equals("/hello")));
        assertTrue(document.paths().items().stream().anyMatch(p -> p.path().equals("/users")));
    }
}
