package io.zupix;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import java.util.List;

/** Performs Jakarta Bean Validation for Zupix request models. */
final class Validator {
    private final ValidatorFactory factory = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory();
    private final jakarta.validation.Validator validator = factory.getValidator();

    void validate(Object value) {
        if (value == null) return;
        var violations = validator.validate(value);
        if (!violations.isEmpty()) {
            List<String> errors = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .sorted()
                    .toList();
            throw new ValidationException(errors);
        }
    }

    void close() { factory.close(); }
}
