package com.example.demo.JobApplication.exception;

public class InvalidJobIdException extends RuntimeException {

    public InvalidJobIdException(String message) {
        super(message);
    }
}
