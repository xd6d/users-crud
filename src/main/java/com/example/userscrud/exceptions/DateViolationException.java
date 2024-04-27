package com.example.userscrud.exceptions;

public class DateViolationException extends RuntimeException {
    public DateViolationException(String message) {
        super(message);
    }
}
