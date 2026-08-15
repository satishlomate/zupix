package io.zupix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ZupixApplicationTest {

    @Test
    void createsApplication() throws Exception {
        var application = new TestApi();
        try (var zupix = ZupixApplication.create(application, 0)) {
            assertNotNull(zupix);
            assertEquals(1, zupix.router().routes().size());
            assertEquals(0, zupix.port());
        }
    }

    static final class TestApi {
        @Get("/hello")
        String hello() {
            return "hello";
        }
    }
}
