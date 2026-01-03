package com.medibridge.user_service.dto;

import com.medibridge.user_service.entity.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * Base DTO for user profiles.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserProfileDTO {
    protected UUID id;
    protected UUID userId;
    protected AccountStatus status;
    protected Long createdAt;
    protected Long updatedAt;
}

