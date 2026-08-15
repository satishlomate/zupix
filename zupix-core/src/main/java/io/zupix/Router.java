package io.zupix;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Route registry used by the Zupix HTTP runtime. */
public final class Router {
    private final List<RegisteredRoute> routes = new ArrayList<>();

    public Router get(String path) {
        return add(new Route("GET", path), null);
    }

    Router add(Route route, RouteHandler handler) {
        Objects.requireNonNull(route, "route");
        routes.add(new RegisteredRoute(route, handler));
        return this;
    }

    RegisteredRoute match(String method, String path) {
        return routes.stream()
                .filter(route -> route.route().method().equals(method))
                .filter(route -> route.route().path().equals(path))
                .findFirst()
                .orElse(null);
    }

    public List<Route> routes() {
        return routes.stream().map(RegisteredRoute::route).toList();
    }

    record RegisteredRoute(Route route, RouteHandler handler) {
    }
}
