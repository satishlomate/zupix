package io.zupix;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/** Invokes a Java method associated with a Zupix route. */
public final class RouteHandler {
    private final Object target;
    private final Method method;
    private final PathParameterResolver pathResolver = new PathParameterResolver();
    private final QueryParameterResolver queryResolver = new QueryParameterResolver();

    public RouteHandler(Object target, Method method) {
        this.target = target;
        this.method = method;
        this.method.setAccessible(true);
    }

    public Object invoke() {
        return invoke(Map.of(), null);
    }

    public Object invoke(Map<String, String> pathParameters, String rawQuery) {
        try {
            Object[] arguments = resolveArguments(pathParameters, rawQuery);
            return method.invoke(target, arguments);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unable to invoke route handler", e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException runtimeException) throw runtimeException;
            throw new IllegalStateException("Route handler failed", cause);
        }
    }

    private Object[] resolveArguments(Map<String, String> pathParameters, String rawQuery) {
        var parameters = method.getParameters();
        Object[] arguments = new Object[parameters.length];
        Object[] path = pathResolver.resolve(method, pathParameters);
        Object[] query = queryResolver.resolve(method, rawQuery);
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(PathParam.class)) arguments[i] = path[i];
            else if (parameters[i].isAnnotationPresent(QueryParam.class)) arguments[i] = query[i];
            else throw new IllegalArgumentException("Unsupported route parameter: " + parameters[i]);
        }
        return arguments;
    }
}
