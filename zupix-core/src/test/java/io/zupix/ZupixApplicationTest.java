package io.zupix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ZupixApplicationTest {

    @Test
    void createsApplication() {
        assertNotNull(ZupixApplication.create());
    }
}
