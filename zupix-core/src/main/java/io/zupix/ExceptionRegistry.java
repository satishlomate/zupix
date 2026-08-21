package io.zupix;

import java.util.LinkedHashMap;
import java.util.Map;

/** Stores exception handlers by exception type. */
public final class ExceptionRegistry {
    private final Map<Class<? extends Throwable>, ExceptionHandler<?>> handlers = new LinkedHashMap<>();

    public <T extends Throwable> ExceptionRegistry on(Class<T> type, ExceptionHandler<T> handler) {
        handlers.put(type, handler); return this;
    }

    public Response handle(Throwable error) {
        for (var entry : handlers.entrySet()) {
            if (entry.getKey().isAssignableFrom(error.getClass())) return invoke(entry.getValue(), error);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> Response invoke(ExceptionHandler<?> handler, Throwable error) {
        return ((ExceptionHandler<T>) handler).handle((T) error);
    }
}
