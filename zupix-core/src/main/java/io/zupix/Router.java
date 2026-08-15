package io.zupix;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Route registry used by the Zupix HTTP runtime. */
public final class Router {
    private final List<RegisteredRoute> routes = new ArrayList<>();

    public Router get(String path) {
        return add(new Route("GET", path), new RouteHandler(new Object() {
            @SuppressWarnings("unused")
            public String handle() {
                return "Zupix route matched";
            }
        }, findDefaultHandler()));
    }

    private static java.lang.reflect.Method findDefaultHandler() {
        try {
            return DefaultRouteHandler.class.getDeclaredMethod("handle");
        } catch (NoSuchMethodException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private static final class DefaultRouteHandler {
        @SuppressWarnings("unused")
        public String handle() {
            return "Zupix route matched";
        }
    }

    Router add(Route route, RouteHandler handler) {
        Objects.requireNonNull(route, "route");
        routes.add(new RegisteredRoute(route, handler, new PathMatcher(route.path())));
        return this;
    }

    MatchedRoute match(String method, String path) {
        return routes.stream()
                .filter(route -> route.route().method().equals(method))
                .map(route -> new MatchedRoute(route, route.matcher().match(path)))
                .filter(matched -> matched.parameters() != null)
                .findFirst()
                .orElse(null);
    }

    public List<Route> routes() {
        return routes.stream().map(RegisteredRoute::route).toList();
    }

    record RegisteredRoute(Route route, RouteHandler handler, PathMatcher matcher) {
    }

    record MatchedRoute(RegisteredRoute route, Map<String, String> parameters) {
    }
}
