package com.medibridge.user_service.service;

import com.medibridge.user_service.dto.AdminProfileDTO;
import com.medibridge.user_service.dto.AdminRegisterRequest;
import com.medibridge.user_service.entity.AdminProfile;
import com.medibridge.user_service.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for managing Admin profiles.
 */
public interface AdminProfileService {

    /**
     * Create admin profile for a user during registration
     */
    AdminProfile createAdminProfile(User user, AdminRegisterRequest request);

    /**
     * Get admin profile by user ID
     */
    Optional<AdminProfile> getAdminProfileByUserId(UUID userId);

    /**
     * Get admin profile by username
     */
    Optional<AdminProfile> getAdminProfileByUsername(String username);

    /**
     * Update admin profile
     */
    AdminProfile updateAdminProfile(UUID userId, AdminProfileDTO dto);

    /**
     * Get all admins in a department
     */
    List<AdminProfile> getAdminsByDepartment(String department);

    /**
     * Get all admins by organization
     */
    List<AdminProfile> getAdminsByOrganization(String organizationCode);

    /**
     * Get all admins with specific access level or higher
     */
    List<AdminProfile> getAdminsWithAccessLevel(Integer accessLevel);

    /**
     * Lock/unlock admin account
     */
    void lockAdminAccount(UUID userId);
    void unlockAdminAccount(UUID userId);

    /**
     * Reset failed login attempts
     */
    void resetFailedLoginAttempts(UUID userId);

    /**
     * Increment failed login attempts
     */
    void incrementFailedLoginAttempts(UUID userId);

    /**
     * Enable/disable MFA for admin
     */
    void enableMFA(UUID userId);
    void disableMFA(UUID userId);

    /**
     * Convert admin profile to DTO
     */
    AdminProfileDTO toDTO(AdminProfile profile);
}

