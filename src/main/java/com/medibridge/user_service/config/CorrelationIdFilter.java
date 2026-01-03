package com.medibridge.user_service.config;

import com.medibridge.user_service.util.CorrelationIdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter to intercept all requests and manage correlation IDs.
 * Ensures correlation IDs are tracked across the entire request lifecycle.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class CorrelationIdFilter extends OncePerRequestFilter {

    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Extract or generate correlation ID
            String correlationId = request.getHeader(CORRELATION_ID_HEADER);
            if (correlationId == null || correlationId.isBlank()) {
                correlationId = CorrelationIdUtil.generateOrGetCorrelationId();
            } else {
                CorrelationIdUtil.setCorrelationId(correlationId);
            }

            // Add correlation ID to response header
            response.setHeader(CORRELATION_ID_HEADER, correlationId);

            log.debug("Request initiated - Path: {}, Method: {}, CorrelationId: {}",
                    request.getRequestURI(), request.getMethod(), correlationId);

            filterChain.doFilter(request, response);
        } finally {
            // Clean up correlation ID from MDC after request completes
            CorrelationIdUtil.clearCorrelationId();
        }
    }
}

