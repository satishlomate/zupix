package io.zupix;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestContextTest {
    @Test
    void headerLookupIsCaseInsensitive() {
        var request = new RequestContext("GET", "/", null, Map.of("Authorization", "Bearer token"), "");
        assertEquals("Bearer token", request.header("authorization"));
    }
}
