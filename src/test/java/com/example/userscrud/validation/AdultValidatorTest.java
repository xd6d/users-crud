package com.example.userscrud.validation;

import com.example.userscrud.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import(TestConfig.class)
class AdultValidatorTest {

    @Autowired
    private AdultValidator validator;

    @Value("${config.age.min}")
    private int minAge;

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