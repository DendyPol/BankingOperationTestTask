package ru.polovinko.bankingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BankingOperationTestTaskApplication {

  public static void main(String[] args) {
    SpringApplication.run(BankingOperationTestTaskApplication.class, args);
  }
}
