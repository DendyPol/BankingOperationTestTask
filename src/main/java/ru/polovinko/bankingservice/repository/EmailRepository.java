package ru.polovinko.bankingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.polovinko.bankingservice.entity.Email;

public interface EmailRepository extends JpaRepository<Email, Long> {
  boolean existsByEmail(String email);
}
