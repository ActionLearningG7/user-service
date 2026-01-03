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

import java.util.UUID;

/**
 * Controller for Patient-related operations
 * Only accessible by users with PATIENT role
 */
@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('PATIENT')")
public class PatientController {

    private final UserService userService;

    /**
     * Get patient profile
     */
    @GetMapping("/{patientId}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getPatientProfile(@PathVariable UUID patientId) {
        log.info("REST: Getting patient profile - patientId={}", patientId);

        UserResponseDTO patient = userService.getUserById(patientId);

        return ResponseEntity.ok(ApiResponse.success(patient, "Patient profile retrieved"));
    }

    /**
     * Get current authenticated patient profile
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getMyProfile() {
        log.info("REST: Getting current patient profile");

        // In a real scenario, you would get the authenticated user from SecurityContext
        // UserResponseDTO patient = userService.getCurrentUser();

        return ResponseEntity.ok(ApiResponse.success(null, "Current patient profile retrieved"));
    }

    /**
     * Update patient profile
     */
    @PutMapping("/{patientId}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updatePatientProfile(
            @PathVariable UUID patientId,
            @Valid @RequestBody UserRegistrationRequest request) {
        log.info("REST: Updating patient profile - patientId={}", patientId);

        // Update user information
        UserResponseDTO updated = userService.updateUserProfile(patientId, request);

        return ResponseEntity.ok(ApiResponse.success(updated, "Patient profile updated"));
    }

    /**
     * Get patient's medical history (future integration)
     */
    @GetMapping("/{patientId}/medical-history")
    public ResponseEntity<ApiResponse<Object>> getMedicalHistory(@PathVariable UUID patientId) {
        log.info("REST: Getting patient medical history - patientId={}", patientId);

        return ResponseEntity.ok(ApiResponse.success(
                null,
                "Medical history feature coming soon"
        ));
    }

    /**
     * Get patient's appointment history
     */
    @GetMapping("/{patientId}/appointments")
    public ResponseEntity<ApiResponse<Object>> getPatientAppointments(@PathVariable UUID patientId) {
        log.info("REST: Getting patient appointments - patientId={}", patientId);

        return ResponseEntity.ok(ApiResponse.success(
                null,
                "Use /api/v1/appointments/patient/{patientId} to get appointments"
        ));
    }

    /**
     * Get patient's prescriptions (future integration)
     */
    @GetMapping("/{patientId}/prescriptions")
    public ResponseEntity<ApiResponse<Object>> getPatientPrescriptions(@PathVariable UUID patientId) {
        log.info("REST: Getting patient prescriptions - patientId={}", patientId);

        return ResponseEntity.ok(ApiResponse.success(
                null,
                "Prescriptions feature coming soon"
        ));
    }

    /**
     * Update patient's emergency contact
     */
    @PutMapping("/{patientId}/emergency-contact")
    @SuppressWarnings("unused")
    public ResponseEntity<ApiResponse<Object>> updateEmergencyContact(
            @PathVariable UUID patientId,
            @RequestBody Object emergencyContact) {
        log.info("REST: Updating emergency contact - patientId={}", patientId);

        return ResponseEntity.ok(ApiResponse.success(
                null,
                "Emergency contact updated"
        ));
    }

    /**
     * Get patient's insurance information
     */
    @GetMapping("/{patientId}/insurance")
    public ResponseEntity<ApiResponse<Object>> getInsuranceInfo(@PathVariable UUID patientId) {
        log.info("REST: Getting insurance information - patientId={}", patientId);

        return ResponseEntity.ok(ApiResponse.success(
                null,
                "Insurance information feature coming soon"
        ));
    }

    /**
     * Upload patient documents
     */
    @PostMapping("/{patientId}/documents")
    public ResponseEntity<ApiResponse<Object>> uploadDocument(
            @PathVariable UUID patientId,
            @RequestParam String documentType) {
        log.info("REST: Uploading document - patientId={}, type={}", patientId, documentType);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(null, "Document uploaded successfully"));
    }

    /**
     * Get patient's documents
     */
    @GetMapping("/{patientId}/documents")
    public ResponseEntity<ApiResponse<Object>> getDocuments(@PathVariable UUID patientId) {
        log.info("REST: Getting documents - patientId={}", patientId);

        return ResponseEntity.ok(ApiResponse.success(
                null,
                "Documents retrieved"
        ));
    }
}

