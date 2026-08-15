package io.zupix;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/** Discovers GET routes from a Zupix application object. */
public final class RouteScanner {

    public List<DiscoveredRoute> scan(Object application) {
        List<DiscoveredRoute> discovered = new ArrayList<>();
        for (Method method : application.getClass().getDeclaredMethods()) {
            Get get = method.getAnnotation(Get.class);
            if (get != null) {
                if (method.getParameterCount() != 0) {
                    throw new IllegalArgumentException(
                            "GET handler currently supports zero parameters: " + method.getName());
                }
                discovered.add(new DiscoveredRoute(
                        new Route("GET", get.value()),
                        new RouteHandler(application, method)));
            }
        }
        return List.copyOf(discovered);
    }

    public record DiscoveredRoute(Route route, RouteHandler handler) {
    }
}
