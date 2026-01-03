package com.medibridge.user_service.util;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Utility class for managing correlation IDs across the application.
 * Uses SLF4J MDC (Mapped Diagnostic Context) for thread-safe correlation ID tracking.
 */
@Component
public class CorrelationIdUtil {

    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
    private static final String CORRELATION_ID_MDC_KEY = "correlationId";

    /**
     * Get the current correlation ID from MDC.
     * @return correlation ID or null if not set
     */
    public static String getCorrelationId() {
        return MDC.get(CORRELATION_ID_MDC_KEY);
    }

    /**
     * Set a correlation ID in MDC.
     * @param correlationId the ID to set
     */
    public static void setCorrelationId(String correlationId) {
        if (correlationId != null && !correlationId.isBlank()) {
            MDC.put(CORRELATION_ID_MDC_KEY, correlationId);
        }
    }

    /**
     * Generate a new correlation ID if one doesn't exist.
     * @return existing or newly generated correlation ID
     */
    public static String generateOrGetCorrelationId() {
        String existing = MDC.get(CORRELATION_ID_MDC_KEY);
        if (existing != null && !existing.isBlank()) {
            return existing;
        }
        String newId = UUID.randomUUID().toString();
        MDC.put(CORRELATION_ID_MDC_KEY, newId);
        return newId;
    }

    /**
     * Clear the correlation ID from MDC.
     */
    public static void clearCorrelationId() {
        MDC.remove(CORRELATION_ID_MDC_KEY);
    }

    /**
     * Get the correlation ID header name.
     * @return header name
     */
    public static String getCorrelationIdHeader() {
        return CORRELATION_ID_HEADER;
    }
}

