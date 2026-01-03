package com.medibridge.user_service.dto;

import com.medibridge.user_service.entity.Role;
import com.medibridge.user_service.validation.NotBlank;
import com.medibridge.user_service.validation.ValidEmail;
import com.medibridge.user_service.validation.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @ValidEmail(message = "Email must be valid")
    private String email;

    @ValidPassword(message = "Password must be at least 8 characters with uppercase, lowercase, digit, and special character")
    private String password;

    private String fullName;

    private String phoneNumber;

    @lombok.Builder.Default
    private Role role = Role.PATIENT;
}
