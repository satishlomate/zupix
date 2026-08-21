package io.zupix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonTest {
    @Test
    void readsRecord() {
        User user = (User) Json.read("{\"name\":\"Satish\"}", User.class);
        assertEquals("Satish", user.name());
    }

    @Test
    void writesObject() {
        assertEquals("{\"name\":\"Satish\"}", Json.write(new User("Satish")));
    }

    @Test
    void rejectsMalformedJson() {
        assertThrows(IllegalArgumentException.class, () -> Json.read("{bad}", User.class));
    }

    record User(String name) {}
}
