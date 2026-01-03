package com.medibridge.user_service.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Admin user profile entity.
 * Contains enterprise-level admin-specific fields.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@DiscriminatorValue("ADMIN")
public class AdminProfile extends UserProfile {

    private String department; // Department name

    private Integer accessLevel; // Access level (1-5: 1=lowest, 5=highest)

    private String permissions; // Comma-separated permissions or JSON string

    private String organizationCode; // Organization/Company code

    private String reportingManager; // UUID or name of reporting manager

    private String location; // Office location/branch

    private String costCenter; // Cost center code for billing purposes

    @lombok.Builder.Default
    private Boolean mfaEnabled = false; // Multi-factor authentication enabled

    @lombok.Builder.Default
    private Boolean auditAccessEnabled = false; // Can access audit logs

    private String ipWhitelist; // Comma-separated IP addresses allowed

    private Long lastActivityTime; // Timestamp of last activity

    @lombok.Builder.Default
    private Integer failedLoginAttempts = 0; // Number of failed login attempts

    @lombok.Builder.Default
    private Boolean accountLocked = false; // Account lock status

    private String approvalChain; // JSON or comma-separated approval chain
}
