package ru.polovinko.bankingservice;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.polovinko.bankingservice.entity.BankAccount;
import ru.polovinko.bankingservice.entity.User;
import ru.polovinko.bankingservice.exception.EntityNotFoundException;
import ru.polovinko.bankingservice.repository.BankAccountRepository;
import ru.polovinko.bankingservice.repository.UserRepository;
import ru.polovinko.bankingservice.service.BankAccountService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BankAccountServiceTest {
  @Container
  public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:12.3")
    .withDatabaseName("user_db")
    .withUsername("postgres")
    .withPassword("3250325q");

  @DynamicPropertySource
  static void properties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
  }

  @Autowired
  private BankAccountService bankAccountService;
  @Autowired
  private BankAccountRepository bankAccountRepository;
  @Autowired
  private UserRepository userRepository;
  private BankAccount fromAccount;
  private BankAccount toAccount;

  @BeforeEach
  public void setUp() {
    var user1 = User.builder()
      .username("user1")
      .password("password1")
      .dateOfBirth(LocalDate.of(1991, 1, 1))
      .build();
    userRepository.save(user1);
    var user2 = User.builder()
      .username("user2")
      .password("password2")
      .dateOfBirth(LocalDate.of(1992, 2, 2))
      .build();
    userRepository.save(user2);
    fromAccount = bankAccountRepository.save(BankAccount.builder()
      .balance(BigDecimal.valueOf(20000))
      .user(user1)
      .build());
    toAccount = bankAccountRepository.save(BankAccount.builder()
      .balance(BigDecimal.valueOf(10000))
      .user(user2)
      .build());
  }

  @AfterEach
  public void tearDown() {
    bankAccountRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  @Transactional
  public void testSuccessfulTransfer() {
    var transferAmount = BigDecimal.valueOf(2000);
    bankAccountService.transferBalance(fromAccount.getId(), toAccount.getId(), transferAmount);
    var updatedFromAccount = bankAccountRepository.findById(fromAccount.getId()).orElseThrow();
    var updatedToAccount = bankAccountRepository.findById(toAccount.getId()).orElseThrow();
    assertAll(
      () -> assertEquals(0, BigDecimal.valueOf(18000).compareTo(updatedFromAccount.getBalance())),
      () -> assertEquals(0, BigDecimal.valueOf(12000).compareTo(updatedToAccount.getBalance()))
    );
  }

  @Test
  @Transactional
  public void testInsufficientFunds() {
    var transferAmount = BigDecimal.valueOf(1000000000);
    var exception = assertThrows(IllegalArgumentException.class, () -> {
      bankAccountService.transferBalance(fromAccount.getId(), toAccount.getId(), transferAmount);
    });
    var expectedMessage = "Insufficient balance";
    var actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  @Transactional
  public void testTransferToNonExistingAccount() {
    var transferAmount = BigDecimal.valueOf(2000);
    var exception = assertThrows(EntityNotFoundException.class, () -> {
      bankAccountService.transferBalance(fromAccount.getId(), 99999L, transferAmount);
    });
    var expectedMessage = "Receiver account not found!";
    var actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  @Transactional
  public void testTransferFromNonExistingAccount() {
    var transferAmount = BigDecimal.valueOf(2000);
    var exception = assertThrows(EntityNotFoundException.class, () -> {
      bankAccountService.transferBalance(99999L, toAccount.getId(), transferAmount);
    });
    var expectedMessage = "Sender account not found!";
    var actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  @Transactional
  public void testNegativeTransferAmount() {
    var transferAmount = BigDecimal.valueOf(-500);
    var exception = assertThrows(IllegalArgumentException.class, () -> {
      bankAccountService.transferBalance(fromAccount.getId(), toAccount.getId(), transferAmount);
    });
    var expectedMessage = "Transfer amount must be positive";
    var actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  @Transactional
  public void testCreateBankAccountForUser() {
    var user3 = User.builder()
      .username("user3")
      .password("password3")
      .dateOfBirth(LocalDate.of(1993, 3, 3))
      .build();
    userRepository.save(user3);
    var bankAccount = BankAccount.builder()
      .balance(BigDecimal.valueOf(15000))
      .user(user3)
      .build();
    bankAccountRepository.save(bankAccount);
    var savedAccount = bankAccountRepository.findById(bankAccount.getId()).orElseThrow();
    assertAll(
      () -> assertEquals(0, BigDecimal.valueOf(15000).compareTo(savedAccount.getBalance())),
      () -> assertEquals(user3.getUsername(), savedAccount.getUser().getUsername())
    );
  }

  @Test
  @Transactional
  public void testDeposit() {
    var depositAmount = BigDecimal.valueOf(5000);
    bankAccountService.deposit(fromAccount.getId(), depositAmount);
    var updatedAccount = bankAccountRepository.findById(fromAccount.getId()).orElseThrow();
    assertEquals(0, BigDecimal.valueOf(25000).compareTo(updatedAccount.getBalance()));
  }

  @Test
  @Transactional
  public void testWithdraw() {
    var withdrawAmount = BigDecimal.valueOf(5000);
    bankAccountService.withdraw(fromAccount.getId(), withdrawAmount);
    var updatedAccount = bankAccountRepository.findById(fromAccount.getId()).orElseThrow();
    assertEquals(0, BigDecimal.valueOf(15000).compareTo(updatedAccount.getBalance()));
  }

  @Test
  @Transactional
  public void testSetBalance() {
    var newBalance = BigDecimal.valueOf(30000);
    var account = bankAccountRepository.findById(fromAccount.getId()).orElseThrow();
    bankAccountService.setBalance(account, newBalance);
    var updatedAccount = bankAccountRepository.findById(fromAccount.getId()).orElseThrow();
    assertEquals(0, BigDecimal.valueOf(30000).compareTo(updatedAccount.getBalance()));
  }

  @Test
  @Transactional
  public void testSetNegativeBalance() {
    var newBalance = BigDecimal.valueOf(-500);
    var account = bankAccountRepository.findById(fromAccount.getId()).orElseThrow();
    var exception = assertThrows(IllegalArgumentException.class, () -> {
      bankAccountService.setBalance(account, newBalance);
    });
    var expectedMessage = "Balance cannot be negative";
    var actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }
}
