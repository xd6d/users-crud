package com.example.userscrud.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

public class AdultValidator implements ConstraintValidator<Adult, LocalDate> {

    @Value("${config.age.min}")
    private int minAge;

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        LocalDate minAgeDate = LocalDate.now().minusYears(minAge);
        return value == null || value.isBefore(minAgeDate);
    }
}
