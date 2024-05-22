package ru.polovinko.bankingservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneNumberAddDTO {
  @NotNull
  private Long userId;
  @NotNull
  private String phoneNumber;
}
