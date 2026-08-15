package io.zupix;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

final class PathParameterResolver {
    Object[] resolve(Method method, Map<String, String> values) {
        Parameter[] parameters = method.getParameters();
        Object[] arguments = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            PathParam annotation = parameters[i].getAnnotation(PathParam.class);
            if (annotation == null) {
                throw new IllegalArgumentException("Every route parameter must use @PathParam");
            }
            String value = values.get(annotation.value());
            if (value == null) {
                throw new IllegalArgumentException("Missing path parameter: " + annotation.value());
            }
            arguments[i] = convert(value, parameters[i].getType());
        }
        return arguments;
    }

    private Object convert(String value, Class<?> type) {
        if (type == String.class) return value;
        if (type == long.class || type == Long.class) return Long.valueOf(value);
        if (type == int.class || type == Integer.class) return Integer.valueOf(value);
        if (type == boolean.class || type == Boolean.class) return Boolean.valueOf(value);
        throw new IllegalArgumentException("Unsupported path parameter type: " + type.getName());
    }
}
