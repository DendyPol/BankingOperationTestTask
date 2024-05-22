package ru.polovinko.bankingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.polovinko.bankingservice.entity.Email;
import ru.polovinko.bankingservice.entity.PhoneNumber;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
public class ClientDTO {
  private Long id;
  private String username;
  private Set<Email> emails;
  private Set<PhoneNumber> phoneNumbers;
  private LocalDate dateOfBirth;
  private BigDecimal balance;
  private Set<String> roles;
}
