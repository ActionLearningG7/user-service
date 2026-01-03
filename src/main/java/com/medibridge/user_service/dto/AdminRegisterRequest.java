package com.medibridge.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Admin registration request with admin-specific profile fields.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AdminRegisterRequest extends RegisterRequest {
    private String department;
    private String organizationCode;
    private String location;
    private String costCenter;

    @lombok.Builder.Default
    private Integer accessLevel = 1; // Default access level
}
