package io.zupix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OpenApiGeneratorTest {
    @Test
    void generatesOpenApiDocumentForRegisteredRoutes() {
        Router router = new Router();
        router.add(new Route("GET", "/hello"), null);
        router.add(new Route("POST", "/users"), null);

        OpenAPI document = new OpenApiGenerator().generate(router);

        assertEquals("3.0.3", document.openapi());
        assertEquals("Zupix API", document.info().title());
        assertEquals("0.1.0", document.info().version());
        assertEquals(2, document.paths().items().size());
        assertTrue(document.paths().items().stream().anyMatch(p -> p.path().equals("/hello")));
        assertTrue(document.paths().items().stream().anyMatch(p -> p.path().equals("/users")));
    }

    @Test
    void groupsMultipleMethodsUnderOnePath() {
        Router router = new Router();
        router.add(new Route("GET", "/items"), null);
        router.add(new Route("PUT", "/items"), null);
        router.add(new Route("DELETE", "/items"), null);

        OpenAPI document = new OpenApiGenerator().generate(router);
        var item = document.paths().items().stream()
                .filter(p -> p.path().equals("/items"))
                .findFirst().orElseThrow();

        assertEquals(3, item.operations().size());
        assertEquals("get", item.operations().get(0).method());
        assertEquals("put", item.operations().get(1).method());
        assertEquals("delete", item.operations().get(2).method());
    }

    @Test
    void generatesJsonDocument() {
        String json = new OpenApiGenerator().generateJson(new Router().get("/hello"));
        assertTrue(json.contains("\"openapi\":\"3.0.3\""));
        assertTrue(json.contains("/hello"));
        assertTrue(json.contains("\"method\":\"get\""));
    }
}
