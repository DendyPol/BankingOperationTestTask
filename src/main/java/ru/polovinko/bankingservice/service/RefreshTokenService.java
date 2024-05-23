package ru.polovinko.bankingservice.service;

import ru.polovinko.bankingservice.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
  Optional<RefreshToken> findByRefreshToken(String token);

  RefreshToken createRefreshToken(Long userId);

  RefreshToken checkRefreshToken(RefreshToken token);

  void deleteByUserId(Long userId);
}
