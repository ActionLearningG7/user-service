package com.medibridge.user_service.exception;

import com.medibridge.user_service.dto.response.ApiResponse;
import com.medibridge.user_service.util.CorrelationIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the User Service.
 * Provides centralized exception handling with consistent error responses.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handle MediBridge domain exceptions
     */
    @ExceptionHandler(MediBridgeException.class)
    public ResponseEntity<ApiResponse<Object>> handleMediBridgeException(
            MediBridgeException ex, WebRequest request) {

        log.warn("MediBridge exception occurred - Code: {}, Message: {}, CorrelationId: {}",
                ex.getErrorCode(), ex.getMessage(), CorrelationIdUtil.getCorrelationId());

        ApiResponse<Object> response = ApiResponse.error(
                ex.getHttpStatus(),
                ex.getMessage(),
                ex.getErrorCode(),
                request.getDescription(false).replace("uri=", "")
        );
        response.setCorrelationId(CorrelationIdUtil.getCorrelationId());

        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getHttpStatus()));
    }

    /**
     * Handle validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });

        log.warn("Validation failed - Errors: {}, CorrelationId: {}",
                fieldErrors, CorrelationIdUtil.getCorrelationId());

        ApiResponse<Object> response = ApiResponse.error(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                "VALIDATION_ERROR",
                request.getDescription(false).replace("uri=", "")
        );
        response.setData(fieldErrors);
        response.setCorrelationId(CorrelationIdUtil.getCorrelationId());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle access denied exceptions
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {

        log.warn("Access denied - Message: {}, CorrelationId: {}",
                ex.getMessage(), CorrelationIdUtil.getCorrelationId());

        ApiResponse<Object> response = ApiResponse.error(
                HttpStatus.FORBIDDEN.value(),
                "Access denied",
                "ACCESS_DENIED",
                request.getDescription(false).replace("uri=", "")
        );
        response.setCorrelationId(CorrelationIdUtil.getCorrelationId());

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    /**
     * Handle type mismatch exceptions
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex, WebRequest request) {

        String error = String.format("Invalid value for parameter '%s': %s",
                ex.getName(), ex.getValue());

        log.warn("Type mismatch - Error: {}, CorrelationId: {}",
                error, CorrelationIdUtil.getCorrelationId());

        ApiResponse<Object> response = ApiResponse.error(
                HttpStatus.BAD_REQUEST.value(),
                error,
                "INVALID_PARAMETER_TYPE",
                request.getDescription(false).replace("uri=", "")
        );
        response.setCorrelationId(CorrelationIdUtil.getCorrelationId());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle 404 Not Found exceptions
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNoHandlerFoundException(
            NoHandlerFoundException ex, WebRequest request) {

        log.warn("Endpoint not found - Path: {}, Method: {}, CorrelationId: {}",
                ex.getRequestURL(), ex.getHttpMethod(), CorrelationIdUtil.getCorrelationId());

        ApiResponse<Object> response = ApiResponse.error(
                HttpStatus.NOT_FOUND.value(),
                "Endpoint not found",
                "NOT_FOUND",
                ex.getRequestURL()
        );
        response.setCorrelationId(CorrelationIdUtil.getCorrelationId());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle generic exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(
            Exception ex, WebRequest request) {

        log.error("Unexpected exception occurred - Message: {}, CorrelationId: {}",
                ex.getMessage(), CorrelationIdUtil.getCorrelationId(), ex);

        ApiResponse<Object> response = ApiResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred. Please contact support.",
                "INTERNAL_SERVER_ERROR",
                request.getDescription(false).replace("uri=", "")
        );
        response.setCorrelationId(CorrelationIdUtil.getCorrelationId());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

