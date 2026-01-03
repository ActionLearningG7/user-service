package com.medibridge.user_service.service;

import com.medibridge.user_service.dto.DoctorProfileDTO;
import com.medibridge.user_service.dto.DoctorRegisterRequest;
import com.medibridge.user_service.entity.DoctorProfile;
import com.medibridge.user_service.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for managing Doctor profiles.
 */
public interface DoctorProfileService {

    /**
     * Create doctor profile for a user during registration
     */
    DoctorProfile createDoctorProfile(User user, DoctorRegisterRequest request);

    /**
     * Get doctor profile by user ID
     */
    Optional<DoctorProfile> getDoctorProfileByUserId(UUID userId);

    /**
     * Get doctor profile by username
     */
    Optional<DoctorProfile> getDoctorProfileByUsername(String username);

    /**
     * Get doctor by license number
     */
    Optional<DoctorProfile> getDoctorByLicenseNumber(String licenseNumber);

    /**
     * Get doctor by NPI number
     */
    Optional<DoctorProfile> getDoctorByNpiNumber(String npiNumber);

    /**
     * Update doctor profile
     */
    DoctorProfile updateDoctorProfile(UUID userId, DoctorProfileDTO dto);

    /**
     * Get all doctors in a specialization
     */
    List<DoctorProfile> getDoctorsBySpecialization(String specialization);

    /**
     * Get all doctors in a department
     */
    List<DoctorProfile> getDoctorsByDepartment(String department);

    /**
     * Get all verified doctors
     */
    List<DoctorProfile> getVerifiedDoctors();

    /**
     * Get verified doctors by specialization
     */
    List<DoctorProfile> getVerifiedDoctorsBySpecialization(String specialization);

    /**
     * Get verified doctors by department, sorted by rating
     */
    List<DoctorProfile> getVerifiedDoctorsByDepartmentSortedByRating(String department);

    /**
     * Get doctors accepting new patients
     */
    List<DoctorProfile> getDoctorsAcceptingNewPatients();

    /**
     * Verify doctor profile
     */
    void verifyDoctor(UUID userId, String verifiedBy);

    /**
     * Reject doctor verification
     */
    void rejectDoctorVerification(UUID userId);

    /**
     * Update doctor availability schedule
     */
    void updateAvailabilitySchedule(UUID userId, String scheduleJson);

    /**
     * Update doctor consultation fee
     */
    void updateConsultationFee(UUID userId, Double fee);

    /**
     * Update doctor rating
     */
    void updateAverageRating(UUID userId, Double rating);

    /**
     * Increment consultation count
     */
    void incrementConsultationCount(UUID userId);

    /**
     * Convert doctor profile to DTO
     */
    DoctorProfileDTO toDTO(DoctorProfile profile);
}

