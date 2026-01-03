package com.medibridge.user_service.dto;

import com.medibridge.user_service.entity.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * DTO for Admin Profile containing enterprise-level admin fields.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AdminProfileDTO extends UserProfileDTO {
    private String department;
    private Integer accessLevel;
    private String permissions;
    private String organizationCode;
    private String reportingManager;
    private String location;
    private String costCenter;
    private Boolean mfaEnabled;
    private Boolean auditAccessEnabled;
    private String ipWhitelist;
    private Long lastActivityTime;
    private Integer failedLoginAttempts;
    private Boolean accountLocked;
    private String approvalChain;
}
