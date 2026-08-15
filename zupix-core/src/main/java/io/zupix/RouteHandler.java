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
    private final Validator validator = new Validator();

    public RouteHandler(Object target, Method method) {
        this.target = target;
        this.method = method;
        this.method.setAccessible(true);
    }

    public Object invoke() { return invoke(Map.of(), null, ""); }
    public Object invoke(Map<String, String> pathParameters, String rawQuery) { return invoke(pathParameters, rawQuery, ""); }

    public Object invoke(Map<String, String> pathParameters, String rawQuery, String body) {
        try {
            return method.invoke(target, resolveArguments(pathParameters, rawQuery, body));
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unable to invoke route handler", e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException runtimeException) throw runtimeException;
            throw new IllegalStateException("Route handler failed", cause);
        }
    }

    private Object[] resolveArguments(Map<String, String> pathParameters, String rawQuery, String body) {
        var parameters = method.getParameters();
        Object[] arguments = new Object[parameters.length];
        Object[] path = pathResolver.resolve(method, pathParameters);
        Object[] query = queryResolver.resolve(method, rawQuery);
        for (int i = 0; i < parameters.length; i++) {
            var parameter = parameters[i];
            if (parameter.isAnnotationPresent(PathParam.class)) arguments[i] = path[i];
            else if (parameter.isAnnotationPresent(QueryParam.class)) arguments[i] = query[i];
            else if (parameter.isAnnotationPresent(Body.class)) {
                arguments[i] = Json.read(body, parameter.getType());
                if (parameter.isAnnotationPresent(Validated.class)) validator.validate(arguments[i]);
            } else throw new IllegalArgumentException("Unsupported route parameter: " + parameter);
        }
        return arguments;
    }
}
