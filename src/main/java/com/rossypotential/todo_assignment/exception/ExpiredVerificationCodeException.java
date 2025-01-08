package com.rossypotential.todo_assignment.exception;

public class ExpiredVerificationCodeException extends RuntimeException {
        public ExpiredVerificationCodeException(String message) {
            super(message);
        }
    }