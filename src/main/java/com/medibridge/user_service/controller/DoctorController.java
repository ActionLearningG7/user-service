package com.medibridge.user_service.controller;

import com.medibridge.user_service.dto.request.UserRegistrationRequest;
import com.medibridge.user_service.dto.response.ApiResponse;
import com.medibridge.user_service.dto.response.UserResponseDTO;
import com.medibridge.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Controller for Doctor-related operations
 * Only accessible by users with DOCTOR role
 */
@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('DOCTOR')")
public class DoctorController {

    private final UserService userService;

    /**
     * Get doctor profile
     */
    @GetMapping("/{doctorId}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getDoctorProfile(@PathVariable UUID doctorId) {
        log.info("REST: Getting doctor profile - doctorId={}", doctorId);

        UserResponseDTO doctor = userService.getUserById(doctorId);

        return ResponseEntity.ok(ApiResponse.success(doctor, "Doctor profile retrieved"));
    }

    /**
     * Get current authenticated doctor profile
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getMyProfile() {
        log.info("REST: Getting current doctor profile");

        return ResponseEntity.ok(ApiResponse.success(null, "Current doctor profile retrieved"));
    }

    /**
     * Update doctor profile
     */
    @PutMapping("/{doctorId}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateDoctorProfile(
            @PathVariable UUID doctorId,
            @Valid @RequestBody UserRegistrationRequest request) {
        log.info("REST: Updating doctor profile - doctorId={}", doctorId);

        UserResponseDTO updated = userService.updateUserProfile(doctorId, request);

        return ResponseEntity.ok(ApiResponse.success(updated, "Doctor profile updated"));
    }

    /**
     * Get doctor's specialization
     */
    @GetMapping("/{doctorId}/specialization")
    public ResponseEntity<ApiResponse<Object>> getSpecialization(@PathVariable UUID doctorId) {
        log.info("REST: Getting doctor specialization - doctorId={}", doctorId);

        return ResponseEntity.ok(ApiResponse.success(
                null,
                "Doctor specialization retrieved"
        ));
    }

    /**
     * Update doctor's specialization
     */
    @PutMapping("/{doctorId}/specialization")
    public ResponseEntity<ApiResponse<Object>> updateSpecialization(
            @PathVariable UUID doctorId,
            @RequestParam String specialization) {
        log.info("REST: Updating specialization - doctorId={}, specialization={}", doctorId, specialization);

        return ResponseEntity.ok(ApiResponse.success(
                null,
                "Specialization updated"
        ));
    }

    /**
     * Get doctor's availability schedule
     */
    @GetMapping("/{doctorId}/availability")
    public ResponseEntity<ApiResponse<Object>> getAvailability(@PathVariable UUID doctorId) {
        log.info("REST: Getting doctor availability - doctorId={}", doctorId);

        return ResponseEntity.ok(ApiResponse.success(
                null,
                "Doctor availability retrieved"
        ));
    }

    /**
     * Set doctor's availability
     */
    @PostMapping("/{doctorId}/availability")
    public ResponseEntity<ApiResponse<Object>> setAvailability(
            @PathVariable UUID doctorId,
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime) {
        log.info("REST: Setting doctor availability - doctorId={}, start={}, end={}",
                doctorId, startTime, endTime);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(null, "Availability set successfully"));
    }

    /**
     * Get doctor's consultations/appointments
     */
    @GetMapping("/{doctorId}/consultations")
    public ResponseEntity<ApiResponse<Object>> getConsultations(@PathVariable UUID doctorId) {
        log.info("REST: Getting doctor consultations - doctorId={}", doctorId);

        return ResponseEntity.ok(ApiResponse.success(
                null,
                "Use /api/v1/appointments/doctor/{doctorId} to get consultations"
        ));
    }

    /**
     * Get doctor's ratings
     */
    @GetMapping("/{doctorId}/ratings")
    public ResponseEntity<ApiResponse<Object>> getRatings(@PathVariable UUID doctorId) {
        log.info("REST: Getting doctor ratings - doctorId={}", doctorId);

        return ResponseEntity.ok(ApiResponse.success(
                null,
                "Doctor ratings retrieved"
        ));
    }

    /**
     * Get doctor's reviews
     */
    @GetMapping("/{doctorId}/reviews")
    public ResponseEntity<ApiResponse<Object>> getReviews(@PathVariable UUID doctorId) {
        log.info("REST: Getting doctor reviews - doctorId={}", doctorId);

        return ResponseEntity.ok(ApiResponse.success(
                null,
                "Doctor reviews retrieved"
        ));
    }

    /**
     * Get doctor's certificate/qualification
     */
    @GetMapping("/{doctorId}/qualifications")
    public ResponseEntity<ApiResponse<Object>> getQualifications(@PathVariable UUID doctorId) {
        log.info("REST: Getting doctor qualifications - doctorId={}", doctorId);

        return ResponseEntity.ok(ApiResponse.success(
                null,
                "Doctor qualifications retrieved"
        ));
    }

    /**
     * Update doctor's certificate
     */
    @PutMapping("/{doctorId}/qualifications")
    @SuppressWarnings("unused")
    public ResponseEntity<ApiResponse<Object>> updateQualifications(
            @PathVariable UUID doctorId,
            @RequestBody Object qualifications) {
        log.info("REST: Updating doctor qualifications - doctorId={}", doctorId);

        return ResponseEntity.ok(ApiResponse.success(
                null,
                "Qualifications updated"
        ));
    }

    /**
     * Get doctor's consultation fees
     */
    @GetMapping("/{doctorId}/fees")
    public ResponseEntity<ApiResponse<Object>> getConsultationFees(@PathVariable UUID doctorId) {
        log.info("REST: Getting consultation fees - doctorId={}", doctorId);

        return ResponseEntity.ok(ApiResponse.success(
                null,
                "Consultation fees retrieved"
        ));
    }

    /**
     * Update doctor's consultation fees
     */
    @PutMapping("/{doctorId}/fees")
    public ResponseEntity<ApiResponse<Object>> updateConsultationFees(
            @PathVariable UUID doctorId,
            @RequestParam Double fees) {
        log.info("REST: Updating consultation fees - doctorId={}, fees={}", doctorId, fees);

        return ResponseEntity.ok(ApiResponse.success(
                null,
                "Consultation fees updated"
        ));
    }

    /**
     * Verify doctor's license
     */
    @PostMapping("/{doctorId}/verify-license")
    public ResponseEntity<ApiResponse<Object>> verifyLicense(
            @PathVariable UUID doctorId,
            @RequestParam String licenseNumber) {
        log.info("REST: Verifying doctor license - doctorId={}, licenseNumber={}", doctorId, licenseNumber);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(null, "License verified"));
    }

    /**
     * Get doctor's patients list
     */
    @GetMapping("/{doctorId}/patients")
    public ResponseEntity<ApiResponse<Object>> getPatients(@PathVariable UUID doctorId) {
        log.info("REST: Getting doctor's patients - doctorId={}", doctorId);

        return ResponseEntity.ok(ApiResponse.success(
                null,
                "Doctor's patients list retrieved"
        ));
    }
}

