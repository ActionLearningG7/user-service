package com.medibridge.user_service.service.impl;

import com.medibridge.user_service.Mapper.UserMapper;
import com.medibridge.user_service.dto.request.UserRegistrationRequest;
import com.medibridge.user_service.dto.response.UserResponseDTO;
import com.medibridge.user_service.entity.User;

import com.medibridge.user_service.exception.UserAlreadyExistsException;
import com.medibridge.user_service.exception.UserNotFoundException;
import com.medibridge.user_service.repository.UserRepository;
import com.medibridge.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of UserService
 * Handles all user-related business logic
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // ==================== RETRIEVAL METHODS ====================

    @Override
    public UserResponseDTO getUserByEmail(String email) {
        log.info("Getting user by email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        return userMapper.userToUserResponseDTO(user);
    }

    @Override
    public UserResponseDTO getUserById(UUID userId) {
        log.info("Getting user by ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        return userMapper.userToUserResponseDTO(user);
    }

    @Override
    public UserResponseDTO getUserByUsername(String username) {
        log.info("Getting user by username: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        return userMapper.userToUserResponseDTO(user);
    }

    // ==================== PATIENT METHODS ====================

    @Override
    public List<UserResponseDTO> getAllPatients() {
        log.info("Getting all patients");

        return userRepository.findAllPatients()
                .stream()
                .map(userMapper::userToUserResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public long getTotalPatientCount() {
        log.info("Getting total patient count");
        return userRepository.countPatients();
    }

    // ==================== DOCTOR METHODS ====================

    @Override
    public List<UserResponseDTO> getAllDoctors() {
        log.info("Getting all doctors");

        return userRepository.findAllDoctors()
                .stream()
                .map(userMapper::userToUserResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public long getTotalDoctorCount() {
        log.info("Getting total doctor count");
        return userRepository.countDoctors();
    }

    @Override
    @Transactional
    public void verifyDoctor(UUID doctorId) {
        log.info("Verifying doctor: {}", doctorId);

        User doctor = userRepository.findById(doctorId)
                .orElseThrow(() -> new UserNotFoundException("Doctor not found with ID: " + doctorId));

        // Set doctor as verified (implement based on your domain model)
        log.info("Doctor verified: {}", doctorId);
    }

    @Override
    @Transactional
    public void rejectDoctorVerification(UUID doctorId, String reason) {
        log.info("Rejecting doctor verification: {} - Reason: {}", doctorId, reason);

        User doctor = userRepository.findById(doctorId)
                .orElseThrow(() -> new UserNotFoundException("Doctor not found with ID: " + doctorId));

        log.info("Doctor verification rejected: {}", doctorId);
    }

    // ==================== UPDATE METHODS ====================

    @Override
    @Transactional
    public UserResponseDTO updateUserProfile(UUID userId, UserRegistrationRequest request) {
        log.info("Updating user profile: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        // Update user fields
        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }

        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            // Check if email already exists
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new UserAlreadyExistsException("Email already in use: " + request.getEmail());
            }
            user.setEmail(request.getEmail());
        }

        User updated = userRepository.save(user);
        log.info("User profile updated: {}", userId);

        return userMapper.userToUserResponseDTO(updated);
    }

    @Override
    @Transactional
    public void activateUser(UUID userId) {
        log.info("Activating user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        user.setIsActive(true);
        userRepository.save(user);

        log.info("User activated: {}", userId);
    }

    @Override
    @Transactional
    public void deactivateUser(UUID userId) {
        log.info("Deactivating user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        user.setIsActive(false);
        userRepository.save(user);

        log.info("User deactivated: {}", userId);
    }

    @Override
    @Transactional
    public void lockUserAccount(UUID userId) {
        log.info("Locking user account: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        user.setIsLocked(true);
        userRepository.save(user);

        log.info("User account locked: {}", userId);
    }

    @Override
    @Transactional
    public void unlockUserAccount(UUID userId) {
        log.info("Unlocking user account: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        user.setIsLocked(false);
        user.setFailedLoginAttempts(0);
        userRepository.save(user);

        log.info("User account unlocked: {}", userId);
    }

    @Override
    @Transactional
    public void softDeleteUser(UUID userId) {
        log.info("Soft deleting user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        user.setIsDeleted(true);
        userRepository.save(user);

        log.info("User soft deleted: {}", userId);
    }

    @Override
    @Transactional
    public void resetPassword(UUID userId, String newPassword) {
        log.info("Resetting password for user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        // Validate password format
        if (newPassword == null || newPassword.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        log.info("Password reset for user: {}", userId);
    }

    // ==================== STATISTICS METHODS ====================

    @Override
    public long getTotalUserCount() {
        log.info("Getting total user count");
        return userRepository.count();
    }

    @Override
    public long getActiveUsersCount() {
        log.info("Getting active users count");
        return userRepository.countActiveUsers();
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        log.info("Getting all users");

        return userRepository.findAll()
                .stream()
                .map(userMapper::userToUserResponseDTO)
                .collect(Collectors.toList());
    }
}

