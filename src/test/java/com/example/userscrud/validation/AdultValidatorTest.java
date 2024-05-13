package com.example.userscrud.validation;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdultValidatorTest {

    private final int minAge = 18;

    private final AdultValidator validator = new AdultValidator(minAge);

    @Test
    void ageAboveMinValidTest() {
        assertTrue(validator.isValid(LocalDate.of(1950, 1, 1), null));
        assertTrue(validator.isValid(LocalDate.of(1990, 1, 1), null));
        assertTrue(validator.isValid(LocalDate.of(2006, 4, 26), null));
        assertTrue(validator.isValid(LocalDate.now().minusYears(minAge).minusDays(1), null));
    }

    @Test
    void ageBelowMinInvalidTest() {
        assertFalse(validator.isValid(LocalDate.now().minusYears(minAge - 1), null));
        assertFalse(validator.isValid(LocalDate.now(), null));
        assertFalse(validator.isValid(LocalDate.now().plusYears(minAge - 1), null));
        assertFalse(validator.isValid(LocalDate.now().plusYears(minAge + 1), null));
    }

}