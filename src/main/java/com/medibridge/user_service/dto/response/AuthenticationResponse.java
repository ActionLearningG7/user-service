package com.medibridge.user_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for user authentication response (login success)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * JWT access token
     */
    private String accessToken;

    /**
     * Refresh token for obtaining new access tokens
     */
    private String refreshToken;

    /**
     * Token type (usually "Bearer")
     */
    private String tokenType;

    /**
     * Access token expiration time (in milliseconds)
     */
    private Long expiresIn;

    /**
     * Authenticated user details
     */
    private UserResponseDTO user;

    /**
     * Token issued timestamp
     */
    private LocalDateTime issuedAt;

    /**
     * Correlation ID for request tracking
     */
    private String correlationId;
}

