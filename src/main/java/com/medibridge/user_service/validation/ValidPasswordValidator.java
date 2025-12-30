package com.medibridge.user_service.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for password strength.
 */
public class ValidPasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword annotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // Let @NotBlank handle null/empty validation
        }

        boolean hasUpperCase = value.matches(".*[A-Z].*");
        boolean hasLowerCase = value.matches(".*[a-z].*");
        boolean hasDigit = value.matches(".*[0-9].*");
        boolean hasSpecialChar = value.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");
        boolean isLengthValid = value.length() >= 8;

        boolean isValid = hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar && isLengthValid;

        if (!isValid) {
            context.disableDefaultConstraintViolation();

            if (!hasUpperCase) {
                context.buildConstraintViolationWithTemplate("Password must contain at least one uppercase letter")
                        .addConstraintViolation();
            }
            if (!hasLowerCase) {
                context.buildConstraintViolationWithTemplate("Password must contain at least one lowercase letter")
                        .addConstraintViolation();
            }
            if (!hasDigit) {
                context.buildConstraintViolationWithTemplate("Password must contain at least one digit")
                        .addConstraintViolation();
            }
            if (!hasSpecialChar) {
                context.buildConstraintViolationWithTemplate("Password must contain at least one special character")
                        .addConstraintViolation();
            }
            if (!isLengthValid) {
                context.buildConstraintViolationWithTemplate("Password must be at least 8 characters")
                        .addConstraintViolation();
            }
        }

        return isValid;
    }
}

