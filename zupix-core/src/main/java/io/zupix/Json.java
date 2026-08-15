package io.zupix;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/** Internal JSON codec used by the Zupix HTTP layer. */
final class Json {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Json() {}

    static Object read(String body, Class<?> type) {
        try {
            return MAPPER.readValue(body, type);
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Invalid JSON request body", exception);
        }
    }

    static String write(Object value) {
        try {
            return MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Unable to serialize response", exception);
        }
    }
}
