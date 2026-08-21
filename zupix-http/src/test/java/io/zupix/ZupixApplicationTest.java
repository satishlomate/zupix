package io.zupix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ZupixApplicationTest {
    @Test
    void createsApplication() throws Exception {
        try (var application = ZupixApplication.create(new TestApi(), 0)) {
            assertNotNull(application);
            assertEquals(1, application.router().routes().size());
            assertTrue(application.port() > 0);
        }
    }

    static final class TestApi {
        @Get("/hello")
        String hello() { return "hello"; }
    }
}
