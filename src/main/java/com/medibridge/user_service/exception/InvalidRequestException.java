package com.medibridge.user_service.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown for invalid or malformed request data.
 */
public class InvalidRequestException extends MediBridgeException {

    private static final long serialVersionUID = 1L;

    public InvalidRequestException(String message) {
        super(message, "INVALID_REQUEST", HttpStatus.BAD_REQUEST.value());
    }

    public InvalidRequestException(String fieldName, String message) {
        super(
            String.format("Invalid request: %s - %s", fieldName, message),
            "INVALID_REQUEST",
            HttpStatus.BAD_REQUEST.value()
        );
    }
}

