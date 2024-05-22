package ru.polovinko.bankingservice.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.polovinko.bankingservice.entity.RoleType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {
  private String username;
  private String password;
  private LocalDate dateOfBirth;
  private String email;
  private String phone;
  private BigDecimal balance;
  private Set<RoleType> roles;
}
