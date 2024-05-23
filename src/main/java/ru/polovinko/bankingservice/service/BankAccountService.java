package ru.polovinko.bankingservice.service;

import org.springframework.transaction.annotation.Transactional;
import ru.polovinko.bankingservice.entity.BankAccount;

import java.math.BigDecimal;

public interface BankAccountService {
  void transferBalance(Long fromAccountId, Long toAccountId, BigDecimal amount);

  void updateBalance();

  @Transactional
  void deposit(Long accountId, BigDecimal amount);

  @Transactional
  void withdraw(Long accountId, BigDecimal amount);

  void setBalance(BankAccount account, BigDecimal balance);
}
