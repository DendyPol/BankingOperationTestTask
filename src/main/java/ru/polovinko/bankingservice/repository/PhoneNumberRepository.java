package ru.polovinko.bankingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.polovinko.bankingservice.entity.PhoneNumber;

public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {
  boolean existsByPhoneNumber(String phoneNumber);
}
