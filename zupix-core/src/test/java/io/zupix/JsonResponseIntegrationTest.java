package io.zupix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonResponseIntegrationTest {
    @Test
    void responseCanCarryStructuredJsonBody() {
        record User(String name, int age) {}
        var response = Response.ok(new User("Satish", 30));

        assertEquals(200, response.status());
        assertEquals("Satish", ((User) response.body()).name());
        assertEquals(30, ((User) response.body()).age());
        User decoded = (User) Json.read(Json.write(response.body()), User.class);
        assertEquals("Satish", decoded.name());
        assertEquals(30, decoded.age());
    }
}
