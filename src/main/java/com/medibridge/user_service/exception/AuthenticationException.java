package com.medibridge.user_service.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown for authentication related errors.
 */
public class AuthenticationException extends MediBridgeException {

    private static final long serialVersionUID = 1L;

    public AuthenticationException(String message) {
        super(message, HttpStatus.UNAUTHORIZED.value(), "AUTHENTICATION_FAILED");
    }

    /**
     * Exception for invalid credentials.
     */
    public static class InvalidCredentialsException extends AuthenticationException {
        private static final long serialVersionUID = 1L;

        public InvalidCredentialsException() {
            super("Invalid username or password");
        }
    }

    /**
     * Exception for locked accounts.
     */
    public static class AccountLockedException extends AuthenticationException {
        private static final long serialVersionUID = 1L;

        public AccountLockedException(String username) {
            super(String.format("Account locked for user: %s", username));
        }
    }

    /**
     * Exception for disabled accounts.
     */
    public static class AccountDisabledException extends AuthenticationException {
        private static final long serialVersionUID = 1L;

        public AccountDisabledException(String username) {
            super(String.format("Account is disabled for user: %s", username));
        }
    }
}

