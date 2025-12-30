package com.medibridge.user_service.service;

import com.medibridge.user_service.entity.RefreshToken;
import com.medibridge.user_service.repository.RefreshTokenRepository;
import com.medibridge.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import com.medibridge.user_service.util.JwtUtils;
import java.util.UUID;

public interface RefreshTokenService {
    public RefreshToken createRefreshToken(String username);
    public Optional<RefreshToken> findByToken(String token);
    public RefreshToken verifyExpiration(RefreshToken token);

}
