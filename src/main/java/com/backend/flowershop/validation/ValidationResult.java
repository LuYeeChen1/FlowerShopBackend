package com.backend.flowershop.validation;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
    private final List<ValidationError> errors = new ArrayList<>();

    public void addError(String code, String message) {
        errors.add(new ValidationError(code, message));
    }

    public boolean isValid() {
        return errors.isEmpty();
    }

    public List<ValidationError> errors() {
        return List.copyOf(errors);
    }
}
