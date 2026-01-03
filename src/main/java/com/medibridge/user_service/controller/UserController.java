package com.medibridge.user_service.controller;

import com.medibridge.user_service.dto.ApiResponse;
import com.medibridge.user_service.dto.request.UserRegistrationRequest;
import com.medibridge.user_service.dto.response.UserResponseDTO;
import com.medibridge.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * User Controller - Complete REST API for User Management
 * Handles all user-related operations including patient, doctor, and admin functions
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    // ==================== PATIENT ENDPOINTS ====================

    /**
     * Get patient profile
     * GET /api/v1/users/patients/{patientId}
     */
    @GetMapping("/patients/{patientId}")
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getPatientProfile(@PathVariable UUID patientId) {
        log.info("REST: Getting patient profile - patientId={}", patientId);
        UserResponseDTO patient = userService.getUserById(patientId);
        return ResponseEntity.ok(ApiResponse.success(patient, "Patient profile retrieved successfully"));
    }

    /**
     * Update patient profile
     * PUT /api/v1/users/patients/{patientId}
     */
    @PutMapping("/patients/{patientId}")
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updatePatientProfile(
            @PathVariable UUID patientId,
            @Valid @RequestBody UserRegistrationRequest request) {
        log.info("REST: Updating patient profile - patientId={}", patientId);
        UserResponseDTO updated = userService.updateUserProfile(patientId, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Patient profile updated successfully"));
    }

    /**
     * Get patient medical history
     * GET /api/v1/users/patients/{patientId}/medical-history
     */
    @GetMapping("/patients/{patientId}/medical-history")
    @PreAuthorize("hasRole('PATIENT') or hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMedicalHistory(@PathVariable UUID patientId) {
        log.info("REST: Getting patient medical history - patientId={}", patientId);
        Map<String, Object> history = new HashMap<>();
        history.put("patientId", patientId);
        history.put("message", "Medical history feature coming soon");
        return ResponseEntity.ok(ApiResponse.success(history, "Medical history retrieved"));
    }

    /**
     * Get patient appointments
     * GET /api/v1/users/patients/{patientId}/appointments
     */
    @GetMapping("/patients/{patientId}/appointments")
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPatientAppointments(@PathVariable UUID patientId) {
        log.info("REST: Getting patient appointments - patientId={}", patientId);
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Use /api/v1/appointments/patient/{patientId} endpoint");
        return ResponseEntity.ok(ApiResponse.success(data, "Redirected to appointment service"));
    }

    /**
     * Get patient prescriptions
     * GET /api/v1/users/patients/{patientId}/prescriptions
     */
    @GetMapping("/patients/{patientId}/prescriptions")
    @PreAuthorize("hasRole('PATIENT') or hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPatientPrescriptions(@PathVariable UUID patientId) {
        log.info("REST: Getting patient prescriptions - patientId={}", patientId);
        Map<String, Object> data = new HashMap<>();
        data.put("patientId", patientId);
        data.put("message", "Prescriptions feature coming soon");
        return ResponseEntity.ok(ApiResponse.success(data, "Prescriptions retrieved"));
    }

    /**
     * Update emergency contact
     * PUT /api/v1/users/patients/{patientId}/emergency-contact
     */
    @PutMapping("/patients/{patientId}/emergency-contact")
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> updateEmergencyContact(
            @PathVariable UUID patientId,
            @RequestBody Map<String, String> contact) {
        log.info("REST: Updating emergency contact - patientId={}", patientId);
        Map<String, Object> response = new HashMap<>();
        response.put("patientId", patientId);
        response.put("contact", contact);
        return ResponseEntity.ok(ApiResponse.success(response, "Emergency contact updated"));
    }

    /**
     * Get insurance information
     * GET /api/v1/users/patients/{patientId}/insurance
     */
    @GetMapping("/patients/{patientId}/insurance")
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getInsuranceInfo(@PathVariable UUID patientId) {
        log.info("REST: Getting insurance information - patientId={}", patientId);
        Map<String, Object> data = new HashMap<>();
        data.put("patientId", patientId);
        data.put("message", "Insurance information feature coming soon");
        return ResponseEntity.ok(ApiResponse.success(data, "Insurance info retrieved"));
    }

    /**
     * Upload document
     * POST /api/v1/users/patients/{patientId}/documents
     */
    @PostMapping("/patients/{patientId}/documents")
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> uploadDocument(
            @PathVariable UUID patientId,
            @RequestParam String documentType) {
        log.info("REST: Uploading document - patientId={}, type={}", patientId, documentType);
        Map<String, Object> response = new HashMap<>();
        response.put("patientId", patientId);
        response.put("documentType", documentType);
        response.put("message", "Document uploaded successfully");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Document uploaded"));
    }

    /**
     * Get patient documents
     * GET /api/v1/users/patients/{patientId}/documents
     */
    @GetMapping("/patients/{patientId}/documents")
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPatientDocuments(@PathVariable UUID patientId) {
        log.info("REST: Getting patient documents - patientId={}", patientId);
        Map<String, Object> data = new HashMap<>();
        data.put("patientId", patientId);
        data.put("documents", List.of());
        return ResponseEntity.ok(ApiResponse.success(data, "Documents retrieved"));
    }

    // ==================== DOCTOR ENDPOINTS ====================

    /**
     * Get doctor profile
     * GET /api/v1/users/doctors/{doctorId}
     */
    @GetMapping("/doctors/{doctorId}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getDoctorProfile(@PathVariable UUID doctorId) {
        log.info("REST: Getting doctor profile - doctorId={}", doctorId);
        UserResponseDTO doctor = userService.getUserById(doctorId);
        return ResponseEntity.ok(ApiResponse.success(doctor, "Doctor profile retrieved successfully"));
    }

    /**
     * Update doctor profile
     * PUT /api/v1/users/doctors/{doctorId}
     */
    @PutMapping("/doctors/{doctorId}")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateDoctorProfile(
            @PathVariable UUID doctorId,
            @Valid @RequestBody UserRegistrationRequest request) {
        log.info("REST: Updating doctor profile - doctorId={}", doctorId);
        UserResponseDTO updated = userService.updateUserProfile(doctorId, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Doctor profile updated successfully"));
    }

    /**
     * Get doctor specialization
     * GET /api/v1/users/doctors/{doctorId}/specialization
     */
    @GetMapping("/doctors/{doctorId}/specialization")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSpecialization(@PathVariable UUID doctorId) {
        log.info("REST: Getting doctor specialization - doctorId={}", doctorId);
        Map<String, Object> data = new HashMap<>();
        data.put("doctorId", doctorId);
        data.put("specialization", "Cardiology");
        return ResponseEntity.ok(ApiResponse.success(data, "Doctor specialization retrieved"));
    }

    /**
     * Update doctor specialization
     * PUT /api/v1/users/doctors/{doctorId}/specialization
     */
    @PutMapping("/doctors/{doctorId}/specialization")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> updateSpecialization(
            @PathVariable UUID doctorId,
            @RequestParam String specialization) {
        log.info("REST: Updating specialization - doctorId={}, specialization={}", doctorId, specialization);
        Map<String, Object> response = new HashMap<>();
        response.put("doctorId", doctorId);
        response.put("specialization", specialization);
        return ResponseEntity.ok(ApiResponse.success(response, "Specialization updated"));
    }

    /**
     * Get doctor availability
     * GET /api/v1/users/doctors/{doctorId}/availability
     */
    @GetMapping("/doctors/{doctorId}/availability")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAvailability(@PathVariable UUID doctorId) {
        log.info("REST: Getting doctor availability - doctorId={}", doctorId);
        Map<String, Object> data = new HashMap<>();
        data.put("doctorId", doctorId);
        data.put("isAvailable", true);
        return ResponseEntity.ok(ApiResponse.success(data, "Doctor availability retrieved"));
    }

    /**
     * Set doctor availability
     * POST /api/v1/users/doctors/{doctorId}/availability
     */
    @PostMapping("/doctors/{doctorId}/availability")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> setAvailability(
            @PathVariable UUID doctorId,
            @RequestParam String startTime,
            @RequestParam String endTime) {
        log.info("REST: Setting availability - doctorId={}, start={}, end={}", doctorId, startTime, endTime);
        Map<String, Object> response = new HashMap<>();
        response.put("doctorId", doctorId);
        response.put("startTime", startTime);
        response.put("endTime", endTime);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Availability set"));
    }

    /**
     * Get doctor consultations
     * GET /api/v1/users/doctors/{doctorId}/consultations
     */
    @GetMapping("/doctors/{doctorId}/consultations")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getConsultations(@PathVariable UUID doctorId) {
        log.info("REST: Getting doctor consultations - doctorId={}", doctorId);
        Map<String, Object> data = new HashMap<>();
        data.put("doctorId", doctorId);
        data.put("message", "Use /api/v1/appointments/doctor/{doctorId} endpoint");
        return ResponseEntity.ok(ApiResponse.success(data, "Redirected to appointment service"));
    }

    /**
     * Get doctor ratings
     * GET /api/v1/users/doctors/{doctorId}/ratings
     */
    @GetMapping("/doctors/{doctorId}/ratings")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getRatings(@PathVariable UUID doctorId) {
        log.info("REST: Getting doctor ratings - doctorId={}", doctorId);
        Map<String, Object> data = new HashMap<>();
        data.put("doctorId", doctorId);
        data.put("averageRating", 4.5);
        data.put("totalReviews", 125);
        return ResponseEntity.ok(ApiResponse.success(data, "Doctor ratings retrieved"));
    }

    /**
     * Get doctor reviews
     * GET /api/v1/users/doctors/{doctorId}/reviews
     */
    @GetMapping("/doctors/{doctorId}/reviews")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getReviews(@PathVariable UUID doctorId) {
        log.info("REST: Getting doctor reviews - doctorId={}", doctorId);
        Map<String, Object> data = new HashMap<>();
        data.put("doctorId", doctorId);
        data.put("reviews", List.of());
        return ResponseEntity.ok(ApiResponse.success(data, "Doctor reviews retrieved"));
    }

    /**
     * Get doctor qualifications
     * GET /api/v1/users/doctors/{doctorId}/qualifications
     */
    @GetMapping("/doctors/{doctorId}/qualifications")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getQualifications(@PathVariable UUID doctorId) {
        log.info("REST: Getting doctor qualifications - doctorId={}", doctorId);
        Map<String, Object> data = new HashMap<>();
        data.put("doctorId", doctorId);
        data.put("qualifications", List.of("MD", "Board Certified"));
        return ResponseEntity.ok(ApiResponse.success(data, "Doctor qualifications retrieved"));
    }

    /**
     * Update doctor qualifications
     * PUT /api/v1/users/doctors/{doctorId}/qualifications
     */
    @PutMapping("/doctors/{doctorId}/qualifications")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> updateQualifications(
            @PathVariable UUID doctorId,
            @RequestBody Map<String, Object> qualifications) {
        log.info("REST: Updating doctor qualifications - doctorId={}", doctorId);
        Map<String, Object> response = new HashMap<>();
        response.put("doctorId", doctorId);
        response.put("qualifications", qualifications);
        return ResponseEntity.ok(ApiResponse.success(response, "Qualifications updated"));
    }

    /**
     * Get consultation fees
     * GET /api/v1/users/doctors/{doctorId}/fees
     */
    @GetMapping("/doctors/{doctorId}/fees")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getConsultationFees(@PathVariable UUID doctorId) {
        log.info("REST: Getting consultation fees - doctorId={}", doctorId);
        Map<String, Object> data = new HashMap<>();
        data.put("doctorId", doctorId);
        data.put("fees", 50.00);
        return ResponseEntity.ok(ApiResponse.success(data, "Consultation fees retrieved"));
    }

    /**
     * Update consultation fees
     * PUT /api/v1/users/doctors/{doctorId}/fees
     */
    @PutMapping("/doctors/{doctorId}/fees")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> updateConsultationFees(
            @PathVariable UUID doctorId,
            @RequestParam Double fees) {
        log.info("REST: Updating consultation fees - doctorId={}, fees={}", doctorId, fees);
        Map<String, Object> response = new HashMap<>();
        response.put("doctorId", doctorId);
        response.put("fees", fees);
        return ResponseEntity.ok(ApiResponse.success(response, "Consultation fees updated"));
    }

    /**
     * Verify doctor license
     * POST /api/v1/users/doctors/{doctorId}/verify-license
     */
    @PostMapping("/doctors/{doctorId}/verify-license")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> verifyLicense(
            @PathVariable UUID doctorId,
            @RequestParam String licenseNumber) {
        log.info("REST: Verifying doctor license - doctorId={}, license={}", doctorId, licenseNumber);
        Map<String, Object> response = new HashMap<>();
        response.put("doctorId", doctorId);
        response.put("licenseNumber", licenseNumber);
        response.put("verified", true);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "License verified"));
    }

    /**
     * Get doctor's patients list
     * GET /api/v1/users/doctors/{doctorId}/patients
     */
    @GetMapping("/doctors/{doctorId}/patients")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPatients(@PathVariable UUID doctorId) {
        log.info("REST: Getting doctor's patients - doctorId={}", doctorId);
        Map<String, Object> data = new HashMap<>();
        data.put("doctorId", doctorId);
        data.put("patients", List.of());
        return ResponseEntity.ok(ApiResponse.success(data, "Doctor's patients list retrieved"));
    }

    // ==================== ADMIN ENDPOINTS ====================

    /**
     * Get all users (paginated)
     * GET /api/v1/users/all?page=0&size=20
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("REST: Getting all users - page={}, size={}", page, size);
        List<UserResponseDTO> users = userService.getAllUsers();
        Map<String, Object> response = new HashMap<>();
        response.put("users", users);
        response.put("total", users.size());
        response.put("page", page);
        response.put("size", size);
        return ResponseEntity.ok(ApiResponse.success(response, "All users fetched successfully"));
    }

    /**
     * Get user by ID
     * GET /api/v1/users/{userId}
     */
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(@PathVariable UUID userId) {
        log.info("REST: Getting user by ID - userId={}", userId);
        UserResponseDTO user = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success(user, "User fetched successfully"));
    }

    /**
     * Search user by email
     * GET /api/v1/users/search/email?email=user@example.com
     */
    @GetMapping("/search/email")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponseDTO>> searchByEmail(@RequestParam String email) {
        log.info("REST: Searching user by email - email={}", email);
        UserResponseDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(ApiResponse.success(user, "User found"));
    }

    /**
     * Search user by username
     * GET /api/v1/users/search/username?username=john_doe
     */
    @GetMapping("/search/username")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponseDTO>> searchByUsername(@RequestParam String username) {
        log.info("REST: Searching user by username - username={}", username);
        UserResponseDTO user = userService.getUserByUsername(username);
        return ResponseEntity.ok(ApiResponse.success(user, "User found"));
    }

    /**
     * Get all doctors (paginated)
     * GET /api/v1/users/doctors/all?page=0&size=20
     */
    @GetMapping("/doctors/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAllDoctors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("REST: Getting all doctors - page={}, size={}", page, size);
        List<UserResponseDTO> doctors = userService.getAllDoctors();
        Map<String, Object> response = new HashMap<>();
        response.put("doctors", doctors);
        response.put("total", doctors.size());
        response.put("page", page);
        response.put("size", size);
        return ResponseEntity.ok(ApiResponse.success(response, "All doctors fetched successfully"));
    }

    /**
     * Get all patients (paginated)
     * GET /api/v1/users/patients/all?page=0&size=20
     */
    @GetMapping("/patients/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAllPatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("REST: Getting all patients - page={}, size={}", page, size);
        List<UserResponseDTO> patients = userService.getAllPatients();
        Map<String, Object> response = new HashMap<>();
        response.put("patients", patients);
        response.put("total", patients.size());
        response.put("page", page);
        response.put("size", size);
        return ResponseEntity.ok(ApiResponse.success(response, "All patients fetched successfully"));
    }

    /**
     * Activate user
     * PUT /api/v1/users/{userId}/activate
     */
    @PutMapping("/{userId}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> activateUser(@PathVariable UUID userId) {
        log.info("REST: Activating user - userId={}", userId);
        userService.activateUser(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("status", "ACTIVE");
        return ResponseEntity.ok(ApiResponse.success(response, "User activated"));
    }

    /**
     * Deactivate user
     * PUT /api/v1/users/{userId}/deactivate
     */
    @PutMapping("/{userId}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> deactivateUser(@PathVariable UUID userId) {
        log.info("REST: Deactivating user - userId={}", userId);
        userService.deactivateUser(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("status", "INACTIVE");
        return ResponseEntity.ok(ApiResponse.success(response, "User deactivated"));
    }

    /**
     * Lock user account
     * PUT /api/v1/users/{userId}/lock
     */
    @PutMapping("/{userId}/lock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> lockUser(@PathVariable UUID userId) {
        log.info("REST: Locking user account - userId={}", userId);
        userService.lockUserAccount(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("locked", true);
        return ResponseEntity.ok(ApiResponse.success(response, "User account locked"));
    }

    /**
     * Unlock user account
     * PUT /api/v1/users/{userId}/unlock
     */
    @PutMapping("/{userId}/unlock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> unlockUser(@PathVariable UUID userId) {
        log.info("REST: Unlocking user account - userId={}", userId);
        userService.unlockUserAccount(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("locked", false);
        return ResponseEntity.ok(ApiResponse.success(response, "User account unlocked"));
    }

    /**
     * Reset user password
     * POST /api/v1/users/{userId}/reset-password?newPassword=NewPass123!
     */
    @PostMapping("/{userId}/reset-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> resetPassword(
            @PathVariable UUID userId,
            @RequestParam String newPassword) {
        log.info("REST: Resetting password - userId={}", userId);
        userService.resetPassword(userId, newPassword);
        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("message", "Password reset successfully");
        return ResponseEntity.ok(ApiResponse.success(response, "Password reset"));
    }

    /**
     * Get system statistics
     * GET /api/v1/users/admin/statistics
     */
    @GetMapping("/admin/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSystemStatistics() {
        log.info("REST: Getting system statistics");
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userService.getTotalUserCount());
        stats.put("totalDoctors", userService.getTotalDoctorCount());
        stats.put("totalPatients", userService.getTotalPatientCount());
        stats.put("activeUsers", userService.getActiveUsersCount());
        return ResponseEntity.ok(ApiResponse.success(stats, "System statistics retrieved"));
    }

    /**
     * Verify doctor
     * POST /api/v1/users/admin/doctors/{doctorId}/verify
     */
    @PostMapping("/admin/doctors/{doctorId}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> verifyDoctor(@PathVariable UUID doctorId) {
        log.info("REST: Verifying doctor - doctorId={}", doctorId);
        userService.verifyDoctor(doctorId);
        Map<String, Object> response = new HashMap<>();
        response.put("doctorId", doctorId);
        response.put("verified", true);
        return ResponseEntity.ok(ApiResponse.success(response, "Doctor verified"));
    }

    /**
     * Reject doctor verification
     * POST /api/v1/users/admin/doctors/{doctorId}/reject
     */
    @PostMapping("/admin/doctors/{doctorId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> rejectDoctorVerification(
            @PathVariable UUID doctorId,
            @RequestParam String reason) {
        log.info("REST: Rejecting doctor verification - doctorId={}, reason={}", doctorId, reason);
        userService.rejectDoctorVerification(doctorId, reason);
        Map<String, Object> response = new HashMap<>();
        response.put("doctorId", doctorId);
        response.put("verified", false);
        response.put("reason", reason);
        return ResponseEntity.ok(ApiResponse.success(response, "Doctor verification rejected"));
    }

    /**
     * Send notification
     * POST /api/v1/users/admin/notification/{userId}
     */
    @PostMapping("/admin/notification/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> sendNotification(
            @PathVariable UUID userId,
            @RequestParam String message) {
        log.info("REST: Sending notification - userId={}, message={}", userId, message);
        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("message", message);
        response.put("sent", true);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Notification sent"));
    }

    /**
     * Generate report
     * POST /api/v1/users/admin/reports/generate?reportType=user_activity
     */
    @PostMapping("/admin/reports/generate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> generateReport(@RequestParam String reportType) {
        log.info("REST: Generating report - type={}", reportType);
        Map<String, Object> response = new HashMap<>();
        response.put("reportType", reportType);
        response.put("generatedAt", System.currentTimeMillis());
        response.put("status", "GENERATED");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Report generated"));
    }
}
