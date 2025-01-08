package com.rossypotential.todo_assignment.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ErrorDTO {
    private HttpStatus httpStatus;
    private String message;
}