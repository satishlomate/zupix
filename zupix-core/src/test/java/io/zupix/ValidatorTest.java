package io.zupix;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatorTest {
    private final Validator validator = new Validator();

    @Test
    void acceptsValidRecord() {
        assertDoesNotThrow(() -> validator.validate(new User("Satish")));
    }

    @Test
    void reportsSortedConstraintViolations() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.validate(new User("")));
        assertEquals(1, exception.errors().size());
        assertEquals("name: must not be blank", exception.errors().getFirst());
    }

    @Test
    void acceptsNullValue() {
        assertDoesNotThrow(() -> validator.validate(null));
    }

    record User(@NotBlank String name, @Size(min = 2) String alias) {
        User(String name) { this(name, "ok"); }
    }
}
