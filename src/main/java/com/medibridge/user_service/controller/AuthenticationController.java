package com.medibridge.user_service.controller;

import com.medibridge.user_service.dto.ApiResponse;
import com.medibridge.user_service.dto.AuthenticationRequest;
import com.medibridge.user_service.dto.AuthenticationResponse;
import com.medibridge.user_service.dto.RegisterRequest;
import com.medibridge.user_service.dto.TokenRefreshRequest;
import com.medibridge.user_service.service.AuthenticationService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication Controller
 * Handles user registration, login, and token refresh
 * All endpoints are publicly accessible (no authentication required)
 */
@RestController
@RequestMapping("/api/v1/users/auth")
@RequiredArgsConstructor
@Slf4j
@PermitAll
public class AuthenticationController {

    private final AuthenticationService service;

    /**
     * User registration endpoint
     * Creates a new user account
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        log.info("REST: User registration - username={}", request.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(service.register(request), "User registered successfully"));
    }

    /**
     * User login endpoint
     * Authenticates user and returns JWT token
     */
    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> authenticate(
            @Valid @RequestBody AuthenticationRequest request) {
        log.info("REST: User authentication - username={}", request.getUsername());
        return ResponseEntity.ok(
                ApiResponse.success(service.authenticate(request), "User authenticated successfully"));
    }

    /**
     * Token refresh endpoint
     * Generates new access token using refresh token
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> refreshToken(
            @Valid @RequestBody TokenRefreshRequest request) {
        log.info("REST: Token refresh requested");
        return ResponseEntity.ok(
                ApiResponse.success(service.refreshToken(request), "Token refreshed successfully"));
    }
}
