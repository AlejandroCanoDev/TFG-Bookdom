package com.example.bookdombackend.exceptions.Libro;

public class LibroAlreadyExistsException extends RuntimeException {
    public LibroAlreadyExistsException(String message) {
        super(message);
    }
}