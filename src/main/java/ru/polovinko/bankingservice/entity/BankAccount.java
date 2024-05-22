package ru.polovinko.bankingservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Entity
@Table(name = "bank_account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankAccount {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private BigDecimal balance;
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  @JsonIgnore
  private User user;

  public void setBalance(BigDecimal balance) {
    if (balance.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Balance cannot be negative");
    }
    this.balance = balance;
  }

  public void deposit(BigDecimal amount) {
    setBalance(this.balance.add(amount));
  }

  public void withdraw(BigDecimal amount) {
    setBalance(this.balance.subtract(amount));
  }
}
