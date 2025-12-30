package com.medibridge.user_service.exception;

/**
 * Base custom exception for MediBridge User Service.
 */
public class MediBridgeException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final int httpStatus;
    private final String errorCode;

    public MediBridgeException(String message, int httpStatus, String errorCode) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public MediBridgeException(String message, Throwable cause, int httpStatus, String errorCode) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

