package io.zupix;

import java.util.HashMap;
import java.util.Map;

/** Simple layered configuration backed by environment variables and system properties. */
public final class Configuration {
    private final Map<String, String> values = new HashMap<>();

    public Configuration() {
        values.putAll(System.getenv());
        System.getProperties().forEach((key, value) -> values.put(String.valueOf(key), String.valueOf(value)));
    }

    public String get(String key) { return values.get(key); }
    public String getOrDefault(String key, String fallback) { return values.getOrDefault(key, fallback); }
    public int getInt(String key, int fallback) {
        String value = values.get(key);
        return value == null ? fallback : Integer.parseInt(value);
    }
    public boolean getBoolean(String key, boolean fallback) {
        String value = values.get(key);
        return value == null ? fallback : Boolean.parseBoolean(value);
    }
}
