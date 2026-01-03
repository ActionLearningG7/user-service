package com.medibridge.user_service.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a user email already exists.
 */
public class UserAlreadyExistsException extends MediBridgeException {

    public UserAlreadyExistsException(String email) {
        super(
                String.format("User with email '%s' already exists", email),
                "USER_ALREADY_EXISTS",
                HttpStatus.CONFLICT.value()
        );
    }

    public UserAlreadyExistsException(String message, String identifier) {
        super(
                message,
                "USER_ALREADY_EXISTS",
                HttpStatus.CONFLICT.value()
        );
    }
}

