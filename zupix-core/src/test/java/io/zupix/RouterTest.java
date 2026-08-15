package io.zupix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RouterTest {

    @Test
    void registersGetRoute() {
        var router = new Router().get("/hello");

        assertEquals(1, router.routes().size());
        assertEquals(new Route("GET", "/hello"), router.routes().getFirst());
    }

    @Test
    void rejectsInvalidRoutePath() {
        assertThrows(IllegalArgumentException.class, () -> new Route("GET", "hello"));
    }
}
