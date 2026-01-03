package com.medibridge.user_service.service.impl;

import com.medibridge.user_service.entity.RefreshToken;
import com.medibridge.user_service.repository.RefreshTokenRepository;
import com.medibridge.user_service.repository.UserRepository;
import com.medibridge.user_service.service.RefreshTokenService;
import com.medibridge.user_service.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${application.security.jwt.refresh-token.expiration}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Override
    public RefreshToken createRefreshToken(String username) {
        var user = userRepository.findByUsername(username).orElseThrow();

        // We generate a JWT for the refresh token
        String token = jwtUtils.generateRefreshToken(user);

        RefreshToken refreshToken = refreshTokenRepository.findByUser(user)
                .map(existingToken -> {
                    existingToken.setToken(token);
                    existingToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
                    return existingToken;
                })
                .orElse(RefreshToken.builder()
                        .user(user)
                        .token(token)
                        .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                        .build());

        return refreshTokenRepository.save(refreshToken);
    }
    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    @Transactional
    public int deleteByUserId(UUID userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }
}
