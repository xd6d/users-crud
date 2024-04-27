package com.example.userscrud.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@code null} elements are considered valid.
 */
@Target(ElementType.FIELD)
@Constraint(validatedBy = AdultValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface Adult {
    String message() default "Invalid age";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
