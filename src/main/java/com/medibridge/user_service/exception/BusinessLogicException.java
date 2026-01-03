package com.medibridge.user_service.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown for business logic violations.
 */
public class BusinessLogicException extends MediBridgeException {

    private static final long serialVersionUID = 1L;

    public BusinessLogicException(String message) {
        super(message, "BUSINESS_LOGIC_ERROR", HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    public BusinessLogicException(String message, String errorCode) {
        super(message, errorCode, HttpStatus.UNPROCESSABLE_ENTITY.value());
    }
}

