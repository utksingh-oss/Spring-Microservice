package com.utkarsh.HotelService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlerResourceNotFoundException(
            ResourceNotFoundException exception
    ) {
        String message = exception.getMessage();
        Map<String, Object> apiResponse = new HashMap<>();
        apiResponse.put("message", exception.getMessage());
        apiResponse.put("success", false);
        apiResponse.put("status", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }
}
