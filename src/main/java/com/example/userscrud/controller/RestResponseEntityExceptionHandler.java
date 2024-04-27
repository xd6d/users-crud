package com.example.userscrud.controller;

import com.example.userscrud.exceptions.DateViolationException;
import com.example.userscrud.model.ExceptionResponse;
import jakarta.persistence.EntityExistsException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler {

    private static final String VALIDATION_ERROR_MESSAGE = "Input data validation error";

    @ExceptionHandler({DateViolationException.class, EntityExistsException.class})
    public ResponseEntity<ExceptionResponse> handleBadRequest(HttpServletRequest req,
                                                              RuntimeException exception) {
        return ResponseEntity.badRequest().body(buildResponse(exception.getMessage(), req));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationError(HttpServletRequest req,
                                                                   MethodArgumentNotValidException exception) {
        var errors = exception.getFieldErrors().stream()
                .filter(fe -> fe.getDefaultMessage() != null)
                .collect(Collectors.groupingBy(FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())));
        return ResponseEntity.badRequest().body(buildResponse(VALIDATION_ERROR_MESSAGE, errors, req));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponse> handleValidationError(HttpServletRequest req,
                                                                   ConstraintViolationException exception) {
        var errors = exception.getConstraintViolations().stream()
                .collect(Collectors.groupingBy(ConstraintViolation::getPropertyPath,
                        Collectors.mapping(ConstraintViolation::getMessage, Collectors.toList())));
        return ResponseEntity.badRequest().body(buildResponse(VALIDATION_ERROR_MESSAGE, errors, req));
    }

    private ExceptionResponse buildResponse(String message, Object data, HttpServletRequest req) {
        return ExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(message)
                .data(data)
                .path(req.getContextPath())
                .build();
    }

    private ExceptionResponse buildResponse(String message, HttpServletRequest req) {
        return ExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(message)
                .path(req.getContextPath())
                .build();
    }
}
