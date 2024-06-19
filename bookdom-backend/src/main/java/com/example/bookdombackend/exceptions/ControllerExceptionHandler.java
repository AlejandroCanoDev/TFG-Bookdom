package com.example.bookdombackend.exceptions;

import com.example.bookdombackend.exceptions.Authorization.BadCredentialsException;
import com.example.bookdombackend.exceptions.Libro.LibroAlreadyExistsException;
import com.example.bookdombackend.exceptions.Libro.LibroDeleteException;
import com.example.bookdombackend.exceptions.Libro.LibroNotFoundException;
import com.example.bookdombackend.exceptions.Libro.LibroUpdateException;
import com.example.bookdombackend.exceptions.Usuario.UsuarioAlreadyExistsException;
import com.example.bookdombackend.exceptions.Usuario.UsuarioDeleteException;
import com.example.bookdombackend.exceptions.Usuario.UsuarioNotFoundException;
import com.example.bookdombackend.exceptions.Usuario.UsuarioUpdateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationArgumentErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errores.put(fieldName, errorMessage);
        });

        logger.error("Error en la validación de los datos: " + ex.getMessage(), ex);

        return new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUsuarioNotFoundError(UsuarioNotFoundException ex) {
        logger.error("Usuario no encontrado: " + ex.getMessage(), ex);
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsuarioAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUsuarioAlreadyExistsError(UsuarioAlreadyExistsException ex) {
        logger.error("Usuario ya existe: " + ex.getMessage(), ex);
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UsuarioDeleteException.class)
    public ResponseEntity<Map<String, String>> handleUsuarioDeleteError(UsuarioDeleteException ex) {
        logger.error("Error al borrar el usuario: " + ex.getMessage(), ex);
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UsuarioUpdateException.class)
    public ResponseEntity<Map<String, String>> handleUsuarioUpdateError(UsuarioUpdateException ex) {
        logger.error("Error al actualizar el usuario: " + ex.getMessage(), ex);
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(LibroNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleLibroNotFoundError(LibroNotFoundException ex) {
        logger.error("Libro no encontrado: " + ex.getMessage(), ex);
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LibroAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleLibroAlreadyExistsError(LibroAlreadyExistsException ex) {
        logger.error("Libro ya existe: " + ex.getMessage(), ex);
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(LibroDeleteException.class)
    public ResponseEntity<Map<String, String>> handleLibroDeleteError(LibroDeleteException ex) {
        logger.error("Error al borrar el libro: " + ex.getMessage(), ex);
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(LibroUpdateException.class)
    public ResponseEntity<Map<String, String>> handleLibroUpdateError(LibroUpdateException ex) {
        logger.error("Error al actualizar el libro: " + ex.getMessage(), ex);
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentialsError(BadCredentialsException ex) {
        logger.error("Error al iniciar sesion: " + ex.getMessage(), ex);
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
