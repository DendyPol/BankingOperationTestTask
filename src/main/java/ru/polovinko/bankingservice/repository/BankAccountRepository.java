package ru.polovinko.bankingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.polovinko.bankingservice.entity.BankAccount;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
  @Query("SELECT a FROM BankAccount a WHERE a.balance < (a.balance * 2.07)")
  List<BankAccount> findAccountsNeedingUpdate();
}
