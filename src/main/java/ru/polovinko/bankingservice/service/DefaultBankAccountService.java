package ru.polovinko.bankingservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.polovinko.bankingservice.entity.BankAccount;
import ru.polovinko.bankingservice.exception.EntityNotFoundException;
import ru.polovinko.bankingservice.repository.BankAccountRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultBankAccountService implements BankAccountService {
  private final BankAccountRepository bankAccountRepository;

  @Transactional
  public void transferBalance(Long fromAccountId, Long toAccountId, BigDecimal amount) {
    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Transfer amount must be positive");
    }
    var fromAccount = bankAccountRepository.findById(fromAccountId)
      .orElseThrow(() -> new EntityNotFoundException("Sender account not found!"));
    var toAccount = bankAccountRepository.findById(toAccountId)
      .orElseThrow(() -> new EntityNotFoundException("Receiver account not found!"));
    if (fromAccount.getBalance().compareTo(amount) < 0) {
      throw new IllegalArgumentException("Insufficient balance");
    }
    fromAccount.withdraw(amount);
    toAccount.deposit(amount);
    bankAccountRepository.save(fromAccount);
    bankAccountRepository.save(toAccount);
  }

  @Transactional
  @Scheduled(fixedRate = 60000)
  public void updateBalance() {
    log.info("Starting balance update for all accounts");
    var accounts = bankAccountRepository.findAccountsNeedingUpdate();
    for (BankAccount account : accounts) {
      var currentBalance = account.getBalance();
      var maxBalance = currentBalance.multiply(BigDecimal.valueOf(2.07));
      var newBalance = currentBalance.multiply(BigDecimal.valueOf(1.05));
      if (newBalance.compareTo(maxBalance) > 0) {
        newBalance = maxBalance;
      }
      account.setBalance(newBalance);
      log.info("Updating balance for account {}: new balance {}", account.getId(), newBalance);
    }
    bankAccountRepository.saveAll(accounts);
    log.info("balance update complete for accounts needing update");
  }
}
