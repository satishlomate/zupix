package io.zupix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;

class RouterTest {
    @Test
    void registersGetRoute() {
        var router = new Router().get("/hello");
        assertEquals(1, router.routes().size());
        assertEquals(new Route("GET", "/hello"), router.routes().getFirst());
    }

    @Test
    void matchesPathParameters() {
        var router = new Router().get("/users/{id}");
        var matched = router.match("GET", "/users/42");
        assertEquals("42", matched.parameters().get("id"));
    }

    @Test
    void doesNotMatchDifferentMethod() {
        var router = new Router().get("/hello");
        assertNull(router.match("POST", "/hello"));
    }

    @Test
    void doesNotMatchPartialPath() {
        var router = new Router().get("/hello");
        assertNull(router.match("GET", "/hello/world"));
    }

    @Test
    void rejectsInvalidRoutePath() {
        assertThrows(IllegalArgumentException.class, () -> new Route("GET", "hello"));
    }
}
