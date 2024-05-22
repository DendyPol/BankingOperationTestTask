package ru.polovinko.bankingservice.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.polovinko.bankingservice.entity.BankAccount;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
  private Long id;
  private String token;
  private String refreshToken;
  private String username;
  private Set<String> email;
  private Set<String> phone;
  private BankAccount bankAccount;
  private List<String> roles;
}
