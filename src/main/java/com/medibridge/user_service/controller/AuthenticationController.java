package com.medibridge.user_service.controller;

import com.medibridge.user_service.dto.ApiResponse;
import com.medibridge.user_service.dto.AuthenticationRequest;
import com.medibridge.user_service.dto.AuthenticationResponse;
import com.medibridge.user_service.dto.RegisterRequest;
import com.medibridge.user_service.dto.TokenRefreshRequest;
import com.medibridge.user_service.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(service.register(request), "User registered successfully"));
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> authenticate(
            @Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success(service.authenticate(request), "User authenticated successfully"));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> refreshToken(
            @Valid @RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success(service.refreshToken(request), "Token refreshed successfully"));
    }
}
