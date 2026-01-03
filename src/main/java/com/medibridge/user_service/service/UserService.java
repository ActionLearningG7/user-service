package com.medibridge.user_service.service;

import com.medibridge.user_service.dto.request.UserRegistrationRequest;
import com.medibridge.user_service.dto.response.UserResponseDTO;
import com.medibridge.user_service.entity.User;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for User management
 */
public interface UserService {

    // ==================== RETRIEVAL METHODS ====================

    /**
     * Get user by email
     */
    UserResponseDTO getUserByEmail(String email);

    /**
     * Get user by ID
     */
    UserResponseDTO getUserById(UUID userId);

    /**
     * Get user by username
     */
    UserResponseDTO getUserByUsername(String username);

    // ==================== PATIENT METHODS ====================

    /**
     * Get all patients
     */
    List<UserResponseDTO> getAllPatients();

    /**
     * Get patient count
     */
    long getTotalPatientCount();

    // ==================== DOCTOR METHODS ====================

    /**
     * Get all doctors
     */
    List<UserResponseDTO> getAllDoctors();

    /**
     * Get doctor count
     */
    long getTotalDoctorCount();

    /**
     * Verify doctor
     */
    void verifyDoctor(UUID doctorId);

    /**
     * Reject doctor verification
     */
    void rejectDoctorVerification(UUID doctorId, String reason);

    // ==================== UPDATE METHODS ====================

    /**
     * Update user profile
     */
    UserResponseDTO updateUserProfile(UUID userId, UserRegistrationRequest request);

    /**
     * Activate user
     */
    void activateUser(UUID userId);

    /**
     * Deactivate user
     */
    void deactivateUser(UUID userId);

    /**
     * Lock user account
     */
    void lockUserAccount(UUID userId);

    /**
     * Unlock user account
     */
    void unlockUserAccount(UUID userId);

    /**
     * Soft delete user
     */
    void softDeleteUser(UUID userId);

    /**
     * Reset password
     */
    void resetPassword(UUID userId, String newPassword);

    // ==================== STATISTICS METHODS ====================

    /**
     * Get total user count
     */
    long getTotalUserCount();

    /**
     * Get active users count
     */
    long getActiveUsersCount();

    /**
     * Get all users
     */
    List<UserResponseDTO> getAllUsers();
}


