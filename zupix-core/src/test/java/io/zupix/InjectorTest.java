package io.zupix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class InjectorTest {
    @Test
    void resolvesRegisteredInstance() {
        Injector injector = new Injector();
        Service service = new Service();
        assertSame(service, injector.register(Service.class, service).get(Service.class));
    }

    @Test
    void constructsAndCachesDependencies() {
        Injector injector = new Injector();
        Consumer first = injector.get(Consumer.class);
        Consumer second = injector.get(Consumer.class);
        assertSame(first, second);
        assertSame(first.service, injector.get(Service.class));
        assertEquals("ok", first.service.value);
    }

    static final class Service {
        final String value = "ok";
    }

    static final class Consumer {
        final Service service;
        Consumer(Service service) { this.service = service; }
    }
}
