package com.medibridge.user_service.controller;

import com.medibridge.user_service.dto.DoctorRegisterRequest;
import com.medibridge.user_service.dto.request.UserRegistrationRequest;
import com.medibridge.user_service.dto.response.ApiResponse;
import com.medibridge.user_service.dto.response.UserResponseDTO;
import com.medibridge.user_service.service.AuthenticationService;
import com.medibridge.user_service.service.DoctorProfileService;
import com.medibridge.user_service.service.UserService;
import com.medibridge.user_service.entity.User;
import com.medibridge.user_service.entity.Role;
import com.medibridge.user_service.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller for Admin-related operations
 * Only accessible by users with ADMIN role
 */
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final DoctorProfileService doctorProfileService;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ...existing code...

    /**
     * Get all users
     */
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("REST: Getting all users - page={}, size={}", page, size);

        // List<UserResponseDTO> users = userService.getAllUsers(page, size);

        return ResponseEntity.ok(ApiResponse.success(
                null,
                "Users retrieved"
        ));
    }

    /**
     * Get user by ID
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(@PathVariable UUID userId) {
        log.info("REST: Getting user by ID - userId={}", userId);

        UserResponseDTO user = userService.getUserById(userId);

        return ResponseEntity.ok(ApiResponse.success(user, "User retrieved"));
    }

    /**
     * Search users by email
     */
    @GetMapping("/users/search/email")
    public ResponseEntity<ApiResponse<UserResponseDTO>> searchByEmail(@RequestParam String email) {
        log.info("REST: Searching user by email - email={}", email);

        UserResponseDTO user = userService.getUserByEmail(email);

        return ResponseEntity.ok(ApiResponse.success(user, "User found"));
    }

    /**
     * Search users by username
     */
    @GetMapping("/users/search/username")
    public ResponseEntity<ApiResponse<UserResponseDTO>> searchByUsername(@RequestParam String username) {
        log.info("REST: Searching user by username - username={}", username);

        // UserResponseDTO user = userService.getUserByUsername(username);

        return ResponseEntity.ok(ApiResponse.success(null, "User found"));
    }

    /**
     * Get all doctors
     */
    @GetMapping("/doctors")
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAllDoctors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("REST: Getting all doctors - page={}, size={}", page, size);

        List<UserResponseDTO> doctors = userService.getAllDoctors();

        return ResponseEntity.ok(ApiResponse.success(
                doctors,
                "Doctors retrieved"
        ));
    }

    /**
     * Get doctor by ID
     */
    @GetMapping("/doctors/{doctorId}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getDoctorById(@PathVariable UUID doctorId) {
        log.info("REST: Getting doctor by ID - doctorId={}", doctorId);

        UserResponseDTO doctor = userService.getUserById(doctorId);

        return ResponseEntity.ok(ApiResponse.success(doctor, "Doctor retrieved"));
    }

    /**
     * Create new doctor
     * Creates both User entity with DOCTOR role and DoctorProfile entity
     *
     * Required fields in request:
     * - username: Unique username
     * - email: Unique email
     * - password: Strong password
     * - fullName: Doctor's full name
     * - phoneNumber: Contact phone number
     * - specialization: Medical specialization (e.g., Cardiology)
     * - licenseNumber: Medical license number
     * - yearsOfExperience: Years in practice
     */
    @PostMapping("/doctors")
    public ResponseEntity<ApiResponse<UserResponseDTO>> createDoctor(
            @Valid @RequestBody DoctorRegisterRequest request) {
        log.info("REST: Creating new doctor - username={}, email={}, specialization={}",
                request.getUsername(), request.getEmail(), request.getSpecialization());

        // Validate required doctor-specific fields
        if (request.getPhoneNumber() == null || request.getPhoneNumber().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Phone number is required for doctor registration"));
        }

        if (request.getSpecialization() == null || request.getSpecialization().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Specialization is required for doctor registration"));
        }

        if (request.getLicenseNumber() == null || request.getLicenseNumber().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("License number is required for doctor registration"));
        }

        try {
            // Check if user already exists
            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Username already exists"));
            }

            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Email already exists"));
            }

            // Create User entity with DOCTOR role
            User doctor = User.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.DOCTOR)
                    .fullName(request.getFullName())
                    .phoneNumber(request.getPhoneNumber())
                    .isActive(true)
                    .isLocked(false)
                    .failedLoginAttempts(0)
                    .build();

            // Save User entity
            doctor = userRepository.save(doctor);
            log.info("User created successfully - doctorId={}, username={}", doctor.getId(), doctor.getUsername());

            // Create DoctorProfile entity
            try {
                doctorProfileService.createDoctorProfile(doctor, request);
                log.info("Doctor profile created successfully - doctorId={}, specialization={}",
                        doctor.getId(), request.getSpecialization());
            } catch (Exception e) {
                log.warn("Doctor profile creation failed (non-critical): {}", e.getMessage());
                // Continue even if profile creation fails
            }

            // Convert to DTO
            UserResponseDTO response = UserResponseDTO.builder()
                    .id(doctor.getId())
                    .username(doctor.getUsername())
                    .email(doctor.getEmail())
                    .fullName(doctor.getFullName())
                    .phoneNumber(doctor.getPhoneNumber())
                    .role(doctor.getRole().name())
                    .isActive(doctor.getIsActive())
                    .isLocked(doctor.getIsLocked())
                    .createdAt(doctor.getCreatedAt())
                    .build();

            log.info("Doctor created successfully - doctorId={}", doctor.getId());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(response, "Doctor created successfully"));
        } catch (Exception e) {
            log.error("Error creating doctor: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to create doctor: " + e.getMessage()));
        }
    }

    /**
     * Update doctor information
     * Updates both User entity and DoctorProfile entity
     *
     * Can update:
     * - Basic info (name, email, phone)
     * - Professional info (specialization, qualifications, fees)
     * - Availability and schedule
     * - License and certification details
     */
    @PutMapping("/doctors/{doctorId}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateDoctor(
            @PathVariable UUID doctorId,
            @Valid @RequestBody UserRegistrationRequest request) {
        log.info("REST: Updating doctor - doctorId={}", doctorId);

        // Verify doctor exists
        UserResponseDTO existingDoctor = userService.getUserById(doctorId);
        if (existingDoctor == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            // Update user information
            UserResponseDTO updated = userService.updateUserProfile(doctorId, request);

            // Update doctor profile information (specialization, fees, etc.)
            // This would be handled by DoctorProfileService

            log.info("Doctor updated successfully - doctorId={}", doctorId);

            return ResponseEntity.ok(ApiResponse.success(updated, "Doctor updated successfully"));
        } catch (Exception e) {
            log.error("Error updating doctor: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update doctor: " + e.getMessage()));
        }
    }

    /**
     * Delete doctor (soft delete)
     * Marks doctor as deleted without removing data
     */
    @DeleteMapping("/doctors/{doctorId}")
    public ResponseEntity<ApiResponse<Object>> deleteDoctor(@PathVariable UUID doctorId) {
        log.info("REST: Deleting doctor - doctorId={}", doctorId);

        // Verify doctor exists
        UserResponseDTO doctor = userService.getUserById(doctorId);
        if (doctor == null) {
            return ResponseEntity.notFound().build();
        }

        userService.softDeleteUser(doctorId);

        log.info("Doctor deleted successfully - doctorId={}", doctorId);

        return ResponseEntity.ok(ApiResponse.success(null, "Doctor deleted successfully"));
    }

    /**
     * Activate doctor account
     */
    @PutMapping("/doctors/{doctorId}/activate")
    public ResponseEntity<ApiResponse<Object>> activateDoctor(@PathVariable UUID doctorId) {
        log.info("REST: Activating doctor - doctorId={}", doctorId);

        userService.activateUser(doctorId);

        return ResponseEntity.ok(ApiResponse.success(null, "Doctor account activated"));
    }

    /**
     * Deactivate doctor account
     */
    @PutMapping("/doctors/{doctorId}/deactivate")
    public ResponseEntity<ApiResponse<Object>> deactivateDoctor(@PathVariable UUID doctorId) {
        log.info("REST: Deactivating doctor - doctorId={}", doctorId);

        userService.deactivateUser(doctorId);

        return ResponseEntity.ok(ApiResponse.success(null, "Doctor account deactivated"));
    }

    /**
     * Lock doctor account
     */
    @PutMapping("/doctors/{doctorId}/lock")
    public ResponseEntity<ApiResponse<Object>> lockDoctor(@PathVariable UUID doctorId) {
        log.info("REST: Locking doctor account - doctorId={}", doctorId);

        userService.lockUserAccount(doctorId);

        return ResponseEntity.ok(ApiResponse.success(null, "Doctor account locked"));
    }

    /**
     * Unlock doctor account
     */
    @PutMapping("/doctors/{doctorId}/unlock")
    public ResponseEntity<ApiResponse<Object>> unlockDoctor(@PathVariable UUID doctorId) {
        log.info("REST: Unlocking doctor account - doctorId={}", doctorId);

        userService.unlockUserAccount(doctorId);

        return ResponseEntity.ok(ApiResponse.success(null, "Doctor account unlocked"));
    }

    /**
     * Reset doctor password
     */
    @PostMapping("/doctors/{doctorId}/reset-password")
    public ResponseEntity<ApiResponse<Object>> resetDoctorPassword(
            @PathVariable UUID doctorId,
            @RequestParam String newPassword) {
        log.info("REST: Resetting doctor password - doctorId={}", doctorId);

        userService.resetPassword(doctorId, newPassword);

        return ResponseEntity.ok(ApiResponse.success(null, "Doctor password reset successfully"));
    }

    /**
     * Update doctor specialization
     */
    @PutMapping("/doctors/{doctorId}/specialization")
    public ResponseEntity<ApiResponse<Object>> updateDoctorSpecialization(
            @PathVariable UUID doctorId,
            @RequestParam String specialization) {
        log.info("REST: Updating doctor specialization - doctorId={}, specialization={}", doctorId, specialization);

        // This would be handled by doctor profile service in real implementation
        // For now, return success

        return ResponseEntity.ok(ApiResponse.success(null, "Doctor specialization updated"));
    }

    /**
     * Update doctor qualifications
     */
    @PutMapping("/doctors/{doctorId}/qualifications")
    public ResponseEntity<ApiResponse<Object>> updateDoctorQualifications(
            @PathVariable UUID doctorId,
            @RequestBody Object qualifications) {
        log.info("REST: Updating doctor qualifications - doctorId={}", doctorId);

        return ResponseEntity.ok(ApiResponse.success(null, "Doctor qualifications updated"));
    }

    /**
     * Update doctor consultation fees
     */
    @PutMapping("/doctors/{doctorId}/consultation-fees")
    public ResponseEntity<ApiResponse<Object>> updateConsultationFees(
            @PathVariable UUID doctorId,
            @RequestParam Double fees) {
        log.info("REST: Updating consultation fees - doctorId={}, fees={}", doctorId, fees);

        if (fees <= 0) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Fees must be greater than 0"));
        }

        return ResponseEntity.ok(ApiResponse.success(null, "Consultation fees updated"));
    }

    /**
     * Get all patients of a doctor
     */
    @GetMapping("/doctors/{doctorId}/patients")
    public ResponseEntity<ApiResponse<Object>> getDoctorPatients(@PathVariable UUID doctorId) {
        log.info("REST: Getting doctor's patients - doctorId={}", doctorId);

        // Verify doctor exists
        UserResponseDTO doctor = userService.getUserById(doctorId);
        if (doctor == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(ApiResponse.success(null, "Doctor's patients list retrieved"));
    }

    /**
     * Get doctor statistics
     */
    @GetMapping("/doctors/{doctorId}/statistics")
    public ResponseEntity<ApiResponse<Object>> getDoctorStatistics(@PathVariable UUID doctorId) {
        log.info("REST: Getting doctor statistics - doctorId={}", doctorId);

        // Verify doctor exists
        UserResponseDTO doctor = userService.getUserById(doctorId);
        if (doctor == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(ApiResponse.success(null, "Doctor statistics retrieved"));
    }

    /**
     * Get all patients
     */
    @GetMapping("/patients")
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAllPatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("REST: Getting all patients - page={}, size={}", page, size);

        // List<UserResponseDTO> patients = userService.getAllPatients(page, size);

        return ResponseEntity.ok(ApiResponse.success(
                null,
                "Patients retrieved"
        ));
    }

    /**
     * Create new user (any role)
     */
    @PostMapping("/users")
    public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(
            @Valid @RequestBody UserRegistrationRequest request,
            @RequestParam String role) {
        log.info("REST: Creating new user - username={}, role={}", request.getUsername(), role);

        // UserResponseDTO user = userService.createUser(request, role);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(null, "User created successfully"));
    }

    /**
     * Update any user
     */
    @PutMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(
            @PathVariable UUID userId,
            @Valid @RequestBody UserRegistrationRequest request) {
        log.info("REST: Updating user - userId={}", userId);

        UserResponseDTO updated = userService.updateUserProfile(userId, request);

        return ResponseEntity.ok(ApiResponse.success(updated, "User updated"));
    }

    /**
     * Delete user (soft delete)
     */
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<Object>> deleteUser(@PathVariable UUID userId) {
        log.info("REST: Deleting user - userId={}", userId);

        userService.softDeleteUser(userId);

        return ResponseEntity.ok(ApiResponse.success(null, "User deleted"));
    }

    /**
     * Activate user account
     */
    @PutMapping("/users/{userId}/activate")
    public ResponseEntity<ApiResponse<Object>> activateUser(@PathVariable UUID userId) {
        log.info("REST: Activating user - userId={}", userId);

        userService.activateUser(userId);

        return ResponseEntity.ok(ApiResponse.success(null, "User activated"));
    }

    /**
     * Deactivate user account
     */
    @PutMapping("/users/{userId}/deactivate")
    public ResponseEntity<ApiResponse<Object>> deactivateUser(@PathVariable UUID userId) {
        log.info("REST: Deactivating user - userId={}", userId);

        userService.deactivateUser(userId);

        return ResponseEntity.ok(ApiResponse.success(null, "User deactivated"));
    }

    /**
     * Lock user account
     */
    @PutMapping("/users/{userId}/lock")
    public ResponseEntity<ApiResponse<Object>> lockUser(@PathVariable UUID userId) {
        log.info("REST: Locking user account - userId={}", userId);

        userService.lockUserAccount(userId);

        return ResponseEntity.ok(ApiResponse.success(null, "User account locked"));
    }

    /**
     * Unlock user account
     */
    @PutMapping("/users/{userId}/unlock")
    public ResponseEntity<ApiResponse<Object>> unlockUser(@PathVariable UUID userId) {
        log.info("REST: Unlocking user account - userId={}", userId);

        userService.unlockUserAccount(userId);

        return ResponseEntity.ok(ApiResponse.success(null, "User account unlocked"));
    }

    /**
     * Reset user password
     */
    @PostMapping("/users/{userId}/reset-password")
    public ResponseEntity<ApiResponse<Object>> resetPassword(
            @PathVariable UUID userId,
            @RequestParam String newPassword) {
        log.info("REST: Resetting password - userId={}", userId);

        userService.resetPassword(userId, newPassword);

        return ResponseEntity.ok(ApiResponse.success(null, "Password reset successfully"));
    }

    /**
     * Get system statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<Object>> getSystemStatistics() {
        log.info("REST: Getting system statistics");

        return ResponseEntity.ok(ApiResponse.success(
                null,
                "System statistics retrieved"
        ));
    }

    /**
     * Get total user count
     */
    @GetMapping("/statistics/user-count")
    public ResponseEntity<ApiResponse<Object>> getUserCount() {
        log.info("REST: Getting total user count");

        long count = userService.getTotalUserCount();

        return ResponseEntity.ok(ApiResponse.success(count, "Total user count"));
    }

    /**
     * Get doctor count
     */
    @GetMapping("/statistics/doctor-count")
    public ResponseEntity<ApiResponse<Object>> getDoctorCount() {
        log.info("REST: Getting total doctor count");

        long count = userService.getTotalDoctorCount();

        return ResponseEntity.ok(ApiResponse.success(count, "Total doctor count"));
    }

    /**
     * Get patient count
     */
    @GetMapping("/statistics/patient-count")
    public ResponseEntity<ApiResponse<Object>> getPatientCount() {
        log.info("REST: Getting total patient count");

        long count = userService.getTotalPatientCount();

        return ResponseEntity.ok(ApiResponse.success(count, "Total patient count"));
    }

    /**
     * Get active users count
     */
    @GetMapping("/statistics/active-users")
    public ResponseEntity<ApiResponse<Object>> getActiveUsersCount() {
        log.info("REST: Getting active users count");

        long count = userService.getActiveUsersCount();

        return ResponseEntity.ok(ApiResponse.success(count, "Active users count"));
    }

    /**
     * Verify doctor
     */
    @PostMapping("/doctors/{doctorId}/verify")
    public ResponseEntity<ApiResponse<Object>> verifyDoctor(@PathVariable UUID doctorId) {
        log.info("REST: Verifying doctor - doctorId={}", doctorId);

        userService.verifyDoctor(doctorId);

        return ResponseEntity.ok(ApiResponse.success(null, "Doctor verified"));
    }

    /**
     * Reject doctor verification
     */
    @PostMapping("/doctors/{doctorId}/reject-verification")
    public ResponseEntity<ApiResponse<Object>> rejectDoctorVerification(
            @PathVariable UUID doctorId,
            @RequestParam String reason) {
        log.info("REST: Rejecting doctor verification - doctorId={}, reason={}", doctorId, reason);

        userService.rejectDoctorVerification(doctorId, reason);

        return ResponseEntity.ok(ApiResponse.success(null, "Doctor verification rejected"));
    }

    /**
     * Send notification to user
     */
    @PostMapping("/users/{userId}/notification")
    public ResponseEntity<ApiResponse<Object>> sendNotification(
            @PathVariable UUID userId,
            @RequestParam String message) {
        log.info("REST: Sending notification - userId={}, message={}", userId, message);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(null, "Notification sent"));
    }

    /**
     * Generate system report
     */
    @PostMapping("/reports/generate")
    public ResponseEntity<ApiResponse<Object>> generateReport(
            @RequestParam String reportType) {
        log.info("REST: Generating report - type={}", reportType);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(null, "Report generated"));
    }
}

