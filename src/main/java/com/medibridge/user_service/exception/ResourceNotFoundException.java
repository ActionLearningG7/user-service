package com.medibridge.user_service.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a resource is not found.
 */
public class ResourceNotFoundException extends MediBridgeException {

    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND.value(), "RESOURCE_NOT_FOUND");
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(
            String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue),
            HttpStatus.NOT_FOUND.value(),
            "RESOURCE_NOT_FOUND"
        );
    }
}

