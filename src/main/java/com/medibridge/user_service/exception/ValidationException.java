package com.medibridge.user_service.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when input validation fails.
 */
public class ValidationException extends MediBridgeException {

    public ValidationException(String message) {
        super(
                message,
                "VALIDATION_ERROR",
                HttpStatus.BAD_REQUEST.value()
        );
    }

    public ValidationException(String message, String field) {
        super(
                String.format("Validation failed for field '%s': %s", field, message),
                "VALIDATION_ERROR",
                HttpStatus.BAD_REQUEST.value()
        );
    }
}

