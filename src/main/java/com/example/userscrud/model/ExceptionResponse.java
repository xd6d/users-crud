package com.example.userscrud.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionResponse {
    private LocalDateTime timestamp;
    private String message;
    private Object data;
    private String path;
}
