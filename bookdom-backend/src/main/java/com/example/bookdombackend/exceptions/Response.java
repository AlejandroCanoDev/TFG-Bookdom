package com.example.bookdombackend.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Data
public class Response
{
    private int code;
    private String messege;
    private Map<String, String> errors;

    private Response(int errorCode, String errorMessage)
    {
        this.code = errorCode;
        this.messege = errorMessage;
        this.errors = new HashMap<>();
    }

    private Response(int errorCode, String errorMessage, Map<String, String> errors)
    {
        this.code = errorCode;
        this.messege = errorMessage;
        this.errors = errors;
    }

    public static Response noErrorResponse(String messege)
    {
        return new Response(HttpStatus.OK.value(), messege);
    }

    public static Response generalError(int code, String message)
    {
        return new Response(code, message);
    }

    public static Response validationError(Map<String, String> errors)
    {
        return new Response(HttpStatus.BAD_REQUEST.value(), "Validation errores: ", errors);
    }

    public static Response notFoundError(String message)
    {
        return new Response(HttpStatus.NOT_FOUND.value(), "Not Found Error: " + message);
    }
}
