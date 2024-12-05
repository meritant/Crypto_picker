package com.example.cryptopicker.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<ErrorResponse> handleRegistrationException(RegistrationException e) {
        ErrorResponse response = new ErrorResponse(
            "REGISTRATION_ERROR",
            e.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        ErrorResponse response = new ErrorResponse(
            "VALIDATION_ERROR",
            "Validation failed: " + errors,
            LocalDateTime.now()
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception e) {
        ErrorResponse response = new ErrorResponse(
            "SERVER_ERROR",
            "An unexpected error occurred: " + e.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.internalServerError().body(response);
    }
}