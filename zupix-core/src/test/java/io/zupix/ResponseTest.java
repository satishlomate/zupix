package io.zupix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResponseTest {
    @Test
    void buildsResponseWithStatusAndHeader() {
        var response = Response.created("ok").header("Location", "/users/1");
        assertEquals(201, response.status());
        assertEquals("/users/1", response.headers().get("Location"));
    }
}
