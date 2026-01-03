package com.medibridge.user_service.dto;

import com.medibridge.user_service.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    @Builder.Default
    private String tokenType = "Bearer";
    private String accessToken;
    private String refreshToken;
    private UUID userId;
    private String username;
    private String email;
    private String fullName;
    private Role role;
    private UserProfileDTO profile; // Role-specific profile information
}


