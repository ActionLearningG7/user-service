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
 * DTO for user profile information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfileDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private UUID userId;
    private String specialty;
    private String qualification;
    private String licenseNumber;
    private String yearsOfExperience;
    private String bio;
    private String profilePhotoUrl;
    private String status;
    private Boolean isVerified;
    private Long createdAt;
    private Long updatedAt;
}

