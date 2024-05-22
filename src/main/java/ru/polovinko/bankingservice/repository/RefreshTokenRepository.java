package ru.polovinko.bankingservice.repository;

import org.springframework.data.repository.CrudRepository;
import ru.polovinko.bankingservice.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String token);

  void deleteByUserId(Long userId);
}
