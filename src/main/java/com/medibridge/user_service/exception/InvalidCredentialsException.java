package com.medibridge.user_service.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when user credentials are invalid.
 */
public class InvalidCredentialsException extends MediBridgeException {

    public InvalidCredentialsException(String message) {
        super(
                message != null ? message : "Invalid credentials provided",
                "INVALID_CREDENTIALS",
                HttpStatus.UNAUTHORIZED.value()
        );
    }

    public InvalidCredentialsException() {
        this("Invalid username or password");
    }
}

