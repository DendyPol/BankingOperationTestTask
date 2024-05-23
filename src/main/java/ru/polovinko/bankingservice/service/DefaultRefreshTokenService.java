package ru.polovinko.bankingservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.polovinko.bankingservice.entity.RefreshToken;
import ru.polovinko.bankingservice.exception.RefreshTokenException;
import ru.polovinko.bankingservice.repository.RefreshTokenRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultRefreshTokenService implements RefreshTokenService {
  @Value("${app.jwt.refreshTokenExpiration}")
  private Duration refreshTokenExpiration;
  private final RefreshTokenRepository refreshTokenRepository;

  @Override
  public Optional<RefreshToken> findByRefreshToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  @Override
  public RefreshToken createRefreshToken(Long userId) {
    var refreshToken = RefreshToken.builder()
      .userId(userId)
      .expiryDate(Instant.now().plusMillis(refreshTokenExpiration.toMillis()))
      .token(UUID.randomUUID().toString())
      .build();
    refreshToken = refreshTokenRepository.save(refreshToken);
    return refreshToken;
  }

  @Override
  public RefreshToken checkRefreshToken(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new RefreshTokenException(token.getToken(), "Refresh token was expired. Repeat signing action");
    }
    return token;
  }

  @Override
  public void deleteByUserId(Long userId) {
    refreshTokenRepository.deleteByUserId(userId);
  }
}
