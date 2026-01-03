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
 * DTO for user response (never expose password or sensitive data)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * User ID
     */
    private UUID id;

    /**
     * Username
     */
    private String username;

    /**
     * Email address
     */
    private String email;

    /**
     * Full name
     */
    private String fullName;

    /**
     * Phone number
     */
    private String phoneNumber;

    /**
     * User role
     */
    private String role;

    /**
     * Account active status
     */
    private Boolean isActive;

    /**
     * Account locked status
     */
    private Boolean isLocked;

    /**
     * Last login timestamp
     */
    private LocalDateTime lastLoginAt;

    /**
     * Entity creation timestamp
     */
    private LocalDateTime createdAt;

    /**
     * Entity last update timestamp
     */
    private LocalDateTime updatedAt;

    /**
     * User profile (if available)
     */
    private UserProfileDTO profile;
}

