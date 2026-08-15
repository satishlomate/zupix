package io.zupix;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/** Invokes a Java method associated with a Zupix route. */
public final class RouteHandler {
    private final Object target;
    private final Method method;
    private final PathParameterResolver parameterResolver = new PathParameterResolver();

    public RouteHandler(Object target, Method method) {
        this.target = target;
        this.method = method;
        this.method.setAccessible(true);
    }

    public Object invoke() {
        return invoke(Map.of());
    }

    public Object invoke(Map<String, String> pathParameters) {
        try {
            return method.invoke(target, parameterResolver.resolve(method, pathParameters));
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unable to invoke route handler", e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }
            throw new IllegalStateException("Route handler failed", cause);
        }
    }
}
