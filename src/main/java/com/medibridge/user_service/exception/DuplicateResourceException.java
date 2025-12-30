package com.medibridge.user_service.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when duplicate resource is created.
 */
public class DuplicateResourceException extends MediBridgeException {

    private static final long serialVersionUID = 1L;

    public DuplicateResourceException(String message) {
        super(message, HttpStatus.CONFLICT.value(), "DUPLICATE_RESOURCE");
    }

    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(
            String.format("%s already exists with %s: '%s'", resourceName, fieldName, fieldValue),
            HttpStatus.CONFLICT.value(),
            "DUPLICATE_RESOURCE"
        );
    }
}

