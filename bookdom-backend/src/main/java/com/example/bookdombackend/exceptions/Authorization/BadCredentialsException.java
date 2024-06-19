package com.example.bookdombackend.exceptions.Authorization;

public class BadCredentialsException extends RuntimeException {
    public BadCredentialsException(String message) {
        super(message);
    }
}
