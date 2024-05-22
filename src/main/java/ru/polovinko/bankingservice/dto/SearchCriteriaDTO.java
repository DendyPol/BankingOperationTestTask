package ru.polovinko.bankingservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SearchCriteriaDTO {
  private LocalDate dateOfBirth;
  private String phone;
  private String fullName;
  private String email;
}
