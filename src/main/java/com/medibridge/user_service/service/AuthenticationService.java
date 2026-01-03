package com.medibridge.user_service.service;

import com.medibridge.user_service.dto.AuthenticationRequest;
import com.medibridge.user_service.dto.AuthenticationResponse;
import com.medibridge.user_service.dto.RegisterRequest;
import com.medibridge.user_service.dto.TokenRefreshRequest;


public interface AuthenticationService {

        public AuthenticationResponse register(RegisterRequest request);
        public AuthenticationResponse authenticate(AuthenticationRequest request);
        public AuthenticationResponse refreshToken(TokenRefreshRequest request);

}
