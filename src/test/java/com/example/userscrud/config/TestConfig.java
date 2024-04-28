package com.example.userscrud.config;

import com.example.userscrud.validation.AdultValidator;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public AdultValidator adultValidator() {
        return new AdultValidator();
    }
}
