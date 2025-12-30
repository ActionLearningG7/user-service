package com.medibridge.user_service.service.impl;

import com.medibridge.user_service.dto.AdminRegisterRequest;
import com.medibridge.user_service.dto.AuthenticationRequest;
import com.medibridge.user_service.dto.AuthenticationResponse;
import com.medibridge.user_service.dto.DoctorRegisterRequest;
import com.medibridge.user_service.dto.PatientRegisterRequest;
import com.medibridge.user_service.dto.RegisterRequest;
import com.medibridge.user_service.dto.TokenRefreshRequest;
import com.medibridge.user_service.entity.RefreshToken;
import com.medibridge.user_service.entity.Role;
import com.medibridge.user_service.entity.User;
import com.medibridge.user_service.entity.UserProfile;
import com.medibridge.user_service.repository.UserRepository;
import com.medibridge.user_service.service.AdminProfileService;
import com.medibridge.user_service.service.AuthenticationService;
import com.medibridge.user_service.service.DoctorProfileService;
import com.medibridge.user_service.service.PatientProfileService;
import com.medibridge.user_service.service.RefreshTokenService;
import com.medibridge.user_service.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final AdminProfileService adminProfileService;
    private final DoctorProfileService doctorProfileService;
    private final PatientProfileService patientProfileService;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        log.info("Registering user: {} with role: {}", request.getUsername(), request.getRole());

        // Determine role
        Role role = request.getRole() != null ? request.getRole() : Role.USER;

        // Build user
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .fullName(request.getFullName() != null ? request.getFullName() : request.getUsername())
                .phoneNumber(request.getPhoneNumber())
                .isActive(true)
                .createdAt(System.currentTimeMillis())
                .build();

        user = repository.save(user);

        // Create role-specific profile
        UserProfile profile = null;
        if (role == Role.ADMIN && request instanceof AdminRegisterRequest) {
            profile = adminProfileService.createAdminProfile(user, (AdminRegisterRequest) request);
        } else if (role == Role.DOCTOR && request instanceof DoctorRegisterRequest) {
            profile = doctorProfileService.createDoctorProfile(user, (DoctorRegisterRequest) request);
        } else {
            // Default to patient profile for USER role or if type doesn't match
            PatientRegisterRequest patientRequest;
            if (request instanceof PatientRegisterRequest) {
                patientRequest = (PatientRegisterRequest) request;
            } else {
                patientRequest = PatientRegisterRequest.builder()
                        .username(request.getUsername())
                        .email(request.getEmail())
                        .password(request.getPassword())
                        .fullName(request.getFullName())
                        .phoneNumber(request.getPhoneNumber())
                        .role(request.getRole())
                        .build();
            }
            profile = patientProfileService.createPatientProfile(user, patientRequest);
        }

        user.setProfile(profile);
        user.setCreatedAt(System.currentTimeMillis());
        user.setUpdatedAt(System.currentTimeMillis());
        user = repository.save(user);

        // Generate tokens
        var jwtToken = jwtUtils.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        return buildAuthenticationResponse(user, jwtToken, refreshToken.getToken());
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.info("Authenticating user: {}", request.getUsername());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));

        var user = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        var jwtToken = jwtUtils.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        // Update last login for patients
        if (user.getRole() == Role.USER && user.getProfile() != null) {
            patientProfileService.updateLastLoginTime(user.getId());
        }

        return buildAuthenticationResponse(user, jwtToken, refreshToken.getToken());
    }

    @Override
    public AuthenticationResponse refreshToken(TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateToken(user);
                    return buildAuthenticationResponse(user, token, requestRefreshToken);
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }

    /**
     * Helper method to build authentication response with profile information
     */
    private AuthenticationResponse buildAuthenticationResponse(User user, String accessToken, String refreshToken) {
        var responseBuilder = AuthenticationResponse.builder()
                .tokenType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole());

        // Add role-specific profile to response
        if (user.getProfile() != null) {
            switch (user.getRole()) {
                case ADMIN:
                    if (user.getProfile() instanceof com.medibridge.user_service.entity.AdminProfile) {
                        responseBuilder.profile(adminProfileService
                                .toDTO((com.medibridge.user_service.entity.AdminProfile) user.getProfile()));
                    }
                    break;
                case DOCTOR:
                    if (user.getProfile() instanceof com.medibridge.user_service.entity.DoctorProfile) {
                        responseBuilder.profile(doctorProfileService
                                .toDTO((com.medibridge.user_service.entity.DoctorProfile) user.getProfile()));
                    }
                    break;
                case USER:
                    if (user.getProfile() instanceof com.medibridge.user_service.entity.PatientProfile) {
                        responseBuilder.profile(patientProfileService
                                .toDTO((com.medibridge.user_service.entity.PatientProfile) user.getProfile()));
                    }
                    break;
            }
        }

        return responseBuilder.build();
    }
}
