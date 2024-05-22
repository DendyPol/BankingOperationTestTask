package ru.polovinko.bankingservice.service;

import java.math.BigDecimal;

public interface BankAccountService {
  void transferBalance(Long fromAccountId, Long toAccountId, BigDecimal amount);

  void updateBalance();
}
