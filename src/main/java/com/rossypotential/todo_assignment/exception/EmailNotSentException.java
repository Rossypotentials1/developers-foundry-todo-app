package com.rossypotential.todo_assignment.exception;

public class EmailNotSentException extends RuntimeException{

    public EmailNotSentException(String message){
        super(message);
    }
}
