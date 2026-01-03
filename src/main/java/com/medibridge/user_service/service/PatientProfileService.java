package com.medibridge.user_service.service;

import com.medibridge.user_service.dto.PatientProfileDTO;
import com.medibridge.user_service.dto.PatientRegisterRequest;
import com.medibridge.user_service.entity.PatientProfile;
import com.medibridge.user_service.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for managing Patient profiles.
 */
public interface PatientProfileService {

    /**
     * Create patient profile for a user during registration
     */
    PatientProfile createPatientProfile(User user, PatientRegisterRequest request);

    /**
     * Get patient profile by user ID
     */
    Optional<PatientProfile> getPatientProfileByUserId(UUID userId);

    /**
     * Get patient profile by username
     */
    Optional<PatientProfile> getPatientProfileByUsername(String username);

    /**
     * Get patient profile by email
     */
    Optional<PatientProfile> getPatientProfileByEmail(String email);

    /**
     * Get patient by insurance policy number
     */
    Optional<PatientProfile> getPatientByInsurancePolicyNumber(String policyNumber);

    /**
     * Update patient profile
     */
    PatientProfile updatePatientProfile(UUID userId, PatientProfileDTO dto);

    /**
     * Get patients with specific blood group
     */
    List<PatientProfile> getPatientsByBloodGroup(String bloodGroup);

    /**
     * Get patients who granted medical history access
     */
    List<PatientProfile> getPatientsWithMedicalHistoryAccess();

    /**
     * Get patients with 2FA enabled
     */
    List<PatientProfile> getPatientsWithTwoFactorAuth();

    /**
     * Get patients with specific medical condition
     */
    List<PatientProfile> getPatientsByMedicalCondition(String condition);

    /**
     * Get patients who consented to GDPR
     */
    List<PatientProfile> getPatientsWithGdprConsent();

    /**
     * Grant/revoke medical history access
     */
    void grantMedicalHistoryAccess(UUID userId);
    void revokeMedicalHistoryAccess(UUID userId);

    /**
     * Enable/disable two-factor authentication
     */
    void enableTwoFactorAuth(UUID userId);
    void disableTwoFactorAuth(UUID userId);

    /**
     * Update last login timestamp
     */
    void updateLastLoginTime(UUID userId);

    /**
     * Update last consultation date
     */
    void updateLastConsultationDate(UUID userId);

    /**
     * Validate GDPR and T&C acceptance
     */
    void acceptGdprAndTerms(UUID userId);

    /**
     * Convert patient profile to DTO
     */
    PatientProfileDTO toDTO(PatientProfile profile);
}

