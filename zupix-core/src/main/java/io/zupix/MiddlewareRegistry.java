package io.zupix;

import java.util.ArrayList;
import java.util.List;

/** Stores application middleware in registration order. */
public final class MiddlewareRegistry {
    private final List<Middleware> middlewares = new ArrayList<>();

    public MiddlewareRegistry use(Middleware middleware) {
        middlewares.add(middleware);
        return this;
    }

    List<Middleware> all() {
        return List.copyOf(middlewares);
    }
}
