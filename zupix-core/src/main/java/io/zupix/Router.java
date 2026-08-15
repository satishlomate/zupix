package io.zupix;

import java.util.ArrayList;
import java.util.List;

/** Minimal route registry. Request dispatching will be added later. */
public final class Router {
    private final List<Route> routes = new ArrayList<>();

    public Router get(String path) {
        routes.add(new Route("GET", path));
        return this;
    }

    public List<Route> routes() {
        return List.copyOf(routes);
    }
}
