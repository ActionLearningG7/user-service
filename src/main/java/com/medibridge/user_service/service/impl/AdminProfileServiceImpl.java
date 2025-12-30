package com.medibridge.user_service.service.impl;

import com.medibridge.user_service.dto.AdminProfileDTO;
import com.medibridge.user_service.dto.AdminRegisterRequest;
import com.medibridge.user_service.entity.AccountStatus;
import com.medibridge.user_service.entity.AdminProfile;
import com.medibridge.user_service.entity.User;
import com.medibridge.user_service.repository.AdminProfileRepository;
import com.medibridge.user_service.service.AdminProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminProfileServiceImpl implements AdminProfileService {

    private final AdminProfileRepository adminProfileRepository;

    @Override
    public AdminProfile createAdminProfile(User user, AdminRegisterRequest request) {
        log.info("Creating admin profile for user: {}", user.getUsername());

        AdminProfile adminProfile = AdminProfile.builder()
                .user(user)
                .department(request.getDepartment())
                .organizationCode(request.getOrganizationCode())
                .location(request.getLocation())
                .costCenter(request.getCostCenter())
                .accessLevel(request.getAccessLevel() != null ? request.getAccessLevel() : 1)
                .status(AccountStatus.PENDING_VERIFICATION)
                .mfaEnabled(false)
                .auditAccessEnabled(false)
                .accountLocked(false)
                .failedLoginAttempts(0)
                .build();

        return adminProfileRepository.save(adminProfile);
    }

    @Override
    public Optional<AdminProfile> getAdminProfileByUserId(UUID userId) {
        return adminProfileRepository.findByUserId(userId);
    }

    @Override
    public Optional<AdminProfile> getAdminProfileByUsername(String username) {
        return adminProfileRepository.findByUsername(username);
    }

    @Override
    public AdminProfile updateAdminProfile(UUID userId, AdminProfileDTO dto) {
        AdminProfile adminProfile = adminProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Admin profile not found for user: " + userId));

        if (dto.getDepartment() != null)
            adminProfile.setDepartment(dto.getDepartment());
        if (dto.getAccessLevel() != null)
            adminProfile.setAccessLevel(dto.getAccessLevel());
        if (dto.getPermissions() != null)
            adminProfile.setPermissions(dto.getPermissions());
        if (dto.getOrganizationCode() != null)
            adminProfile.setOrganizationCode(dto.getOrganizationCode());
        if (dto.getReportingManager() != null)
            adminProfile.setReportingManager(dto.getReportingManager());
        if (dto.getLocation() != null)
            adminProfile.setLocation(dto.getLocation());
        if (dto.getCostCenter() != null)
            adminProfile.setCostCenter(dto.getCostCenter());
        if (dto.getMfaEnabled() != null)
            adminProfile.setMfaEnabled(dto.getMfaEnabled());
        if (dto.getAuditAccessEnabled() != null)
            adminProfile.setAuditAccessEnabled(dto.getAuditAccessEnabled());
        if (dto.getIpWhitelist() != null)
            adminProfile.setIpWhitelist(dto.getIpWhitelist());
        if (dto.getStatus() != null)
            adminProfile.setStatus(dto.getStatus());

        log.info("Updated admin profile for user: {}", userId);
        return adminProfileRepository.save(adminProfile);
    }

    @Override
    public List<AdminProfile> getAdminsByDepartment(String department) {
        return adminProfileRepository.findByDepartment(department);
    }

    @Override
    public List<AdminProfile> getAdminsByOrganization(String organizationCode) {
        return adminProfileRepository.findByOrganizationCode(organizationCode);
    }

    @Override
    public List<AdminProfile> getAdminsWithAccessLevel(Integer accessLevel) {
        return adminProfileRepository.findByAccessLevelGreaterThanEqual(accessLevel);
    }

    @Override
    public void lockAdminAccount(UUID userId) {
        AdminProfile adminProfile = adminProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Admin profile not found for user: " + userId));
        adminProfile.setAccountLocked(true);
        adminProfile.setStatus(AccountStatus.SUSPENDED);
        adminProfileRepository.save(adminProfile);
        log.info("Locked admin account for user: {}", userId);
    }

    @Override
    public void unlockAdminAccount(UUID userId) {
        AdminProfile adminProfile = adminProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Admin profile not found for user: " + userId));
        adminProfile.setAccountLocked(false);
        adminProfile.setStatus(AccountStatus.ACTIVE);
        adminProfile.setFailedLoginAttempts(0);
        adminProfileRepository.save(adminProfile);
        log.info("Unlocked admin account for user: {}", userId);
    }

    @Override
    public void resetFailedLoginAttempts(UUID userId) {
        AdminProfile adminProfile = adminProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Admin profile not found for user: " + userId));
        adminProfile.setFailedLoginAttempts(0);
        adminProfileRepository.save(adminProfile);
    }

    @Override
    public void incrementFailedLoginAttempts(UUID userId) {
        AdminProfile adminProfile = adminProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Admin profile not found for user: " + userId));
        int attempts = adminProfile.getFailedLoginAttempts() != null ? adminProfile.getFailedLoginAttempts() : 0;
        adminProfile.setFailedLoginAttempts(attempts + 1);

        // Lock account after 5 failed attempts
        if (adminProfile.getFailedLoginAttempts() >= 5) {
            lockAdminAccount(userId);
        } else {
            adminProfileRepository.save(adminProfile);
        }
    }

    @Override
    public void enableMFA(UUID userId) {
        AdminProfile adminProfile = adminProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Admin profile not found for user: " + userId));
        adminProfile.setMfaEnabled(true);
        adminProfileRepository.save(adminProfile);
        log.info("Enabled MFA for admin user: {}", userId);
    }

    @Override
    public void disableMFA(UUID userId) {
        AdminProfile adminProfile = adminProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Admin profile not found for user: " + userId));
        adminProfile.setMfaEnabled(false);
        adminProfileRepository.save(adminProfile);
        log.info("Disabled MFA for admin user: {}", userId);
    }

    @Override
    public AdminProfileDTO toDTO(AdminProfile profile) {
        if (profile == null)
            return null;

        return AdminProfileDTO.builder()
                .id(profile.getId())
                .userId(profile.getUser().getId())
                .status(profile.getStatus())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .department(profile.getDepartment())
                .accessLevel(profile.getAccessLevel())
                .permissions(profile.getPermissions())
                .organizationCode(profile.getOrganizationCode())
                .reportingManager(profile.getReportingManager())
                .location(profile.getLocation())
                .costCenter(profile.getCostCenter())
                .mfaEnabled(profile.getMfaEnabled())
                .auditAccessEnabled(profile.getAuditAccessEnabled())
                .ipWhitelist(profile.getIpWhitelist())
                .lastActivityTime(profile.getLastActivityTime())
                .failedLoginAttempts(profile.getFailedLoginAttempts())
                .accountLocked(profile.getAccountLocked())
                .approvalChain(profile.getApprovalChain())
                .build();
    }
}
