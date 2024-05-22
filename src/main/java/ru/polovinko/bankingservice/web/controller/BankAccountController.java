package ru.polovinko.bankingservice.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.polovinko.bankingservice.service.DefaultBankAccountService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class BankAccountController {
  private final DefaultBankAccountService defaultBankAccountService;

  @PostMapping("/transfer")
  public ResponseEntity<String> transferBalance(@RequestParam Long fromAccountId,
                                                @RequestParam Long toAccountId,
                                                @RequestParam BigDecimal amount) {
    defaultBankAccountService.transferBalance(fromAccountId, toAccountId, amount);
    return ResponseEntity.ok("Transfer successful");
  }
}
