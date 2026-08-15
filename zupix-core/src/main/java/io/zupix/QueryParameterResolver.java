package io.zupix;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

final class QueryParameterResolver {
    Object[] resolve(Method method, String rawQuery) {
        Map<String, String> values = parse(rawQuery);
        Parameter[] parameters = method.getParameters();
        Object[] arguments = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            QueryParam annotation = parameters[i].getAnnotation(QueryParam.class);
            if (annotation == null) {
                arguments[i] = null;
                continue;
            }
            String value = values.get(annotation.value());
            arguments[i] = value == null ? null : convert(value, parameters[i].getType());
        }
        return arguments;
    }

    private Map<String, String> parse(String rawQuery) {
        Map<String, String> values = new HashMap<>();
        if (rawQuery == null || rawQuery.isBlank()) return values;
        for (String pair : rawQuery.split("&")) {
            String[] parts = pair.split("=", 2);
            String key = decode(parts[0]);
            String value = parts.length == 2 ? decode(parts[1]) : "";
            values.put(key, value);
        }
        return values;
    }

    private String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    private Object convert(String value, Class<?> type) {
        if (type == String.class) return value;
        if (type == long.class || type == Long.class) return Long.valueOf(value);
        if (type == int.class || type == Integer.class) return Integer.valueOf(value);
        if (type == boolean.class || type == Boolean.class) return Boolean.valueOf(value);
        throw new IllegalArgumentException("Unsupported query parameter type: " + type.getName());
    }
}
