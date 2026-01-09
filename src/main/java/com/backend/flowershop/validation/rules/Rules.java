package com.backend.flowershop.validation.rules;

import com.backend.flowershop.validation.ValidationResult;

public interface Rules<T> {
    ValidationResult validate(T input);
}
