package com.medibridge.user_service.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a requested user is not found.
 */
public class UserNotFoundException extends MediBridgeException {

    public UserNotFoundException(String identifier) {
        super(
                String.format("User not found with identifier: %s", identifier),
                "USER_NOT_FOUND",
                HttpStatus.NOT_FOUND.value()
        );
    }

    public UserNotFoundException(String message, String identifier) {
        super(
                message,
                "USER_NOT_FOUND",
                HttpStatus.NOT_FOUND.value()
        );
    }
}

