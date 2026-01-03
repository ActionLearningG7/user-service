package com.medibridge.user_service.service;

import com.medibridge.user_service.exception.AuthenticationException;
import com.medibridge.user_service.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service for handling authentication-related business logic and security constraints.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationConstraintService {

    @Value("${application.error.max-password-attempts:5}")
    private int maxPasswordAttempts;

    @Value("${application.error.account-lock-duration-minutes:30}")
    private int accountLockDurationMinutes;

    /**
     * Validates username format.
     */
    public void validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            log.warn("Username validation failed: Username is empty");
            throw new BusinessLogicException("Username cannot be empty", "INVALID_USERNAME");
        }

        if (username.length() < 3) {
            log.warn("Username validation failed: Username too short - {}", username);
            throw new BusinessLogicException("Username must be at least 3 characters", "USERNAME_TOO_SHORT");
        }

        if (username.length() > 50) {
            log.warn("Username validation failed: Username too long - {}", username);
            throw new BusinessLogicException("Username must not exceed 50 characters", "USERNAME_TOO_LONG");
        }

        if (!username.matches("^[a-zA-Z0-9._-]+$")) {
            log.warn("Username validation failed: Invalid format - {}", username);
            throw new BusinessLogicException(
                    "Username can only contain letters, numbers, dots, underscores, and hyphens",
                    "INVALID_USERNAME_FORMAT"
            );
        }

        log.debug("Username validation passed: {}", username);
    }

    /**
     * Validates email format.
     */
    public void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            log.warn("Email validation failed: Email is empty");
            throw new BusinessLogicException("Email cannot be empty", "INVALID_EMAIL");
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!email.matches(emailRegex)) {
            log.warn("Email validation failed: Invalid format - {}", email);
            throw new BusinessLogicException("Email format is invalid", "INVALID_EMAIL_FORMAT");
        }

        log.debug("Email validation passed: {}", email);
    }

    /**
     * Validates password strength.
     */
    public void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            log.warn("Password validation failed: Password is empty or null");
            throw new BusinessLogicException("Password cannot be empty", "INVALID_PASSWORD");
        }

        if (password.length() < 8) {
            log.warn("Password validation failed: Password too short");
            throw new BusinessLogicException("Password must be at least 8 characters", "PASSWORD_TOO_SHORT");
        }

        if (password.length() > 128) {
            log.warn("Password validation failed: Password too long");
            throw new BusinessLogicException("Password must not exceed 128 characters", "PASSWORD_TOO_LONG");
        }

        boolean hasUpperCase = password.matches(".*[A-Z].*");
        boolean hasLowerCase = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*[0-9].*");
        boolean hasSpecialChar = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");

        if (!hasUpperCase) {
            log.warn("Password validation failed: Missing uppercase letter");
            throw new BusinessLogicException(
                    "Password must contain at least one uppercase letter",
                    "PASSWORD_MISSING_UPPERCASE"
            );
        }

        if (!hasLowerCase) {
            log.warn("Password validation failed: Missing lowercase letter");
            throw new BusinessLogicException(
                    "Password must contain at least one lowercase letter",
                    "PASSWORD_MISSING_LOWERCASE"
            );
        }

        if (!hasDigit) {
            log.warn("Password validation failed: Missing digit");
            throw new BusinessLogicException(
                    "Password must contain at least one digit",
                    "PASSWORD_MISSING_DIGIT"
            );
        }

        if (!hasSpecialChar) {
            log.warn("Password validation failed: Missing special character");
            throw new BusinessLogicException(
                    "Password must contain at least one special character",
                    "PASSWORD_MISSING_SPECIAL_CHAR"
            );
        }

        log.debug("Password validation passed");
    }

    /**
     * Checks if max password attempts exceeded.
     */
    public boolean isMaxAttemptsExceeded(int attempts) {
        return attempts >= maxPasswordAttempts;
    }

    /**
     * Gets the max password attempt limit.
     */
    public int getMaxPasswordAttempts() {
        return maxPasswordAttempts;
    }

    /**
     * Gets account lock duration in minutes.
     */
    public int getAccountLockDurationMinutes() {
        return accountLockDurationMinutes;
    }

    /**
     * Throws exception if credentials are invalid.
     */
    public void validateCredentials(boolean isValid) {
        if (!isValid) {
            log.warn("Credential validation failed: Invalid credentials provided");
            throw new AuthenticationException.InvalidCredentialsException();
        }
        log.debug("Credential validation passed");
    }

    /**
     * Throws exception if account is locked.
     */
    public void validateAccountNotLocked(boolean isLocked, String username) {
        if (isLocked) {
            log.warn("Account validation failed: Account locked for user - {}", username);
            throw new AuthenticationException.AccountLockedException(username);
        }
        log.debug("Account lock validation passed for user: {}", username);
    }

    /**
     * Throws exception if account is disabled.
     */
    public void validateAccountActive(boolean isActive, String username) {
        if (!isActive) {
            log.warn("Account validation failed: Account disabled for user - {}", username);
            throw new AuthenticationException.AccountDisabledException(username);
        }
        log.debug("Account active validation passed for user: {}", username);
    }
}

