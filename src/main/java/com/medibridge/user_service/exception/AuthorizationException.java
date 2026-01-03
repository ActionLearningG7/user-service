package com.medibridge.user_service.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when authorization fails.
 */
public class AuthorizationException extends MediBridgeException {

    private static final long serialVersionUID = 1L;

    public AuthorizationException(String message) {
        super(message, "AUTHORIZATION_FAILED", HttpStatus.FORBIDDEN.value());
    }

    public AuthorizationException(String username, String resource) {
        super(
            String.format("User '%s' does not have permission to access '%s'", username, resource),
            "AUTHORIZATION_FAILED",
            HttpStatus.FORBIDDEN.value()
        );
    }
}

