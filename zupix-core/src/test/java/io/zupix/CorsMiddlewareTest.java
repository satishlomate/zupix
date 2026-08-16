package io.zupix;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CorsMiddlewareTest {
    @Test
    void exposesConfiguredCorsPolicy() {
        var cors = new CorsMiddleware("https://example.com", Set.of("GET", "OPTIONS"), Set.of("Authorization", "Content-Type"));
        assertEquals("https://example.com", cors.allowOrigin());
        assertEquals(Set.of("GET", "OPTIONS"), cors.methods());
        assertEquals(Set.of("Authorization", "Content-Type"), cors.headers());
        assertEquals("GET, OPTIONS", cors.allowMethodsHeader());
        assertEquals("Authorization, Content-Type", cors.allowHeadersHeader());
    }
}
