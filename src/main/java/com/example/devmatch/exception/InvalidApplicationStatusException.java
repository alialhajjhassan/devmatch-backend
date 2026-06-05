package com.example.devmatch.exception;

public class InvalidApplicationStatusException extends RuntimeException {

    public InvalidApplicationStatusException(String message) {
        super(message);
    }
}