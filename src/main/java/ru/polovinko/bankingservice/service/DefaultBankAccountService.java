package ru.polovinko.bankingservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.polovinko.bankingservice.entity.BankAccount;
import ru.polovinko.bankingservice.exception.EntityNotFoundException;
import ru.polovinko.bankingservice.repository.BankAccountRepository;

import java.math.BigDecimal;
import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultBankAccountService implements BankAccountService {
  private final BankAccountRepository bankAccountRepository;
  @Value("${app.bankAccount.balanceUpdateRate}")
  private Duration balanceUpdateRate;
  @Value("${app.bankAccount.maxBalanceMultiplier}")
  private BigDecimal maxBalanceMultiplier;
  @Value("${app.bankAccount.newBalanceMultiplier}")
  private BigDecimal newBalanceMultiplier;

  @Override
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
    withdraw(fromAccount, amount);
    deposit(toAccount, amount);
  }

  @Override
  @Transactional
  @Scheduled(fixedRateString = "${app.bankAccount.balanceUpdateRate}")
  public void updateBalance() {
    log.info("Starting balance update for all accounts");
    var accounts = bankAccountRepository.findAccountsNeedingUpdate();
    for (BankAccount account : accounts) {
      var currentBalance = account.getBalance();
      var maxBalance = currentBalance.multiply(maxBalanceMultiplier);
      var newBalance = currentBalance.multiply(newBalanceMultiplier);
      if (newBalance.compareTo(maxBalance) > 0) {
        newBalance = maxBalance;
      }
      setBalance(account, newBalance);
      log.info("Updating balance for account {}: new balance {}", account.getId(), newBalance);
    }
    bankAccountRepository.saveAll(accounts);
    log.info("Balance update complete for accounts needing update");
  }

  @Override
  @Transactional
  public void deposit(Long accountId, BigDecimal amount) {
    var account = bankAccountRepository.findById(accountId)
      .orElseThrow(() -> new EntityNotFoundException("BankAccount not found with id " + accountId));
    deposit(account, amount);
  }

  @Override
  @Transactional
  public void withdraw(Long accountId, BigDecimal amount) {
    var account = bankAccountRepository.findById(accountId)
      .orElseThrow(() -> new EntityNotFoundException("BankAccount not found with id " + accountId));
    withdraw(account, amount);
  }

  private void deposit(BankAccount account, BigDecimal amount) {
    setBalance(account, account.getBalance().add(amount));
  }

  private void withdraw(BankAccount account, BigDecimal amount) {
    setBalance(account, account.getBalance().subtract(amount));
  }

  @Override
  public void setBalance(BankAccount account, BigDecimal balance) {
    if (balance.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Balance cannot be negative");
    }
    account.setBalance(balance);
    bankAccountRepository.save(account);
  }
}
