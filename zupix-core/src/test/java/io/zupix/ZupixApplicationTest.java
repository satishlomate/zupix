package io.zupix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ZupixApplicationTest {

    @Test
    void createsApplication() throws Exception {
        var application = new TestApi();
        try (var zupix = ZupixApplication.create(application, 0)) {
            assertNotNull(zupix);
            assertEquals(1, zupix.router().routes().size());
            assertTrue(zupix.port() > 0);
        }
    }

    static final class TestApi {
        @Get("/hello")
        String hello() {
            return "hello";
        }
    }
}
