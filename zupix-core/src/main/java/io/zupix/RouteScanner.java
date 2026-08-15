package io.zupix;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/** Discovers HTTP routes from a Zupix application object. */
public final class RouteScanner {
    public List<DiscoveredRoute> scan(Object application) {
        List<DiscoveredRoute> discovered = new ArrayList<>();
        for (Method method : application.getClass().getDeclaredMethods()) {
            add(discovered, application, method, Get.class, "GET");
            add(discovered, application, method, Post.class, "POST");
            add(discovered, application, method, Put.class, "PUT");
            add(discovered, application, method, Patch.class, "PATCH");
            add(discovered, application, method, Delete.class, "DELETE");
        }
        return List.copyOf(discovered);
    }

    private <A extends java.lang.annotation.Annotation> void add(
            List<DiscoveredRoute> routes, Object target, Method method,
            Class<A> annotationType, String httpMethod) {
        A annotation = method.getAnnotation(annotationType);
        if (annotation == null) return;
        String path;
        try {
            path = (String) annotationType.getMethod("value").invoke(annotation);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Invalid route annotation", exception);
        }
        routes.add(new DiscoveredRoute(new Route(httpMethod, path), new RouteHandler(target, method)));
    }

    public record DiscoveredRoute(Route route, RouteHandler handler) {
    }
}
