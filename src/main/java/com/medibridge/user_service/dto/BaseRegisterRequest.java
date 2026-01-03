package com.medibridge.user_service.dto;

import com.medibridge.user_service.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Base registration request containing common fields.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BaseRegisterRequest {
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String phoneNumber;
    private Role role;
}

