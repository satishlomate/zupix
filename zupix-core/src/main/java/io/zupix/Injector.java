package io.zupix;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Small constructor-based dependency injection container. */
public final class Injector {
    private final Map<Class<?>, Object> instances = new ConcurrentHashMap<>();

    public <T> Injector register(Class<T> type, T instance) {
        instances.put(type, instance);
        return this;
    }

    public <T> T get(Class<T> type) {
        Object existing = instances.get(type);
        if (existing != null) return type.cast(existing);
        return type.cast(instances.computeIfAbsent(type, this::construct));
    }

    private Object construct(Class<?> type) {
        try {
            Constructor<?>[] constructors = type.getDeclaredConstructors();
            if (constructors.length != 1) {
                throw new IllegalArgumentException("Service must have exactly one constructor: " + type.getName());
            }
            Constructor<?> constructor = constructors[0];
            constructor.setAccessible(true);
            Object[] dependencies = java.util.Arrays.stream(constructor.getParameterTypes())
                    .map(this::get)
                    .toArray();
            return constructor.newInstance(dependencies);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Unable to construct service: " + type.getName(), exception);
        }
    }
}
