package com.medibridge.user_service.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when duplicate resource is created.
 */
public class DuplicateResourceException extends MediBridgeException {

    private static final long serialVersionUID = 1L;

    public DuplicateResourceException(String message) {
        super(message, "DUPLICATE_RESOURCE", HttpStatus.CONFLICT.value());
    }

    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(
            String.format("%s already exists with %s: '%s'", resourceName, fieldName, fieldValue),
            "DUPLICATE_RESOURCE",
            HttpStatus.CONFLICT.value()
        );
    }
}

