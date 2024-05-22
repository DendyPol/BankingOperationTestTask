package ru.polovinko.bankingservice.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ru.polovinko.bankingservice.dto.EmailAddDTO;
import ru.polovinko.bankingservice.dto.EmailRemoveDTO;
import ru.polovinko.bankingservice.dto.PhoneNumberAddDTO;
import ru.polovinko.bankingservice.dto.PhoneNumberRemoveDTO;

public interface ContactInformationService {
  void addPhoneNumber(@NotNull @Valid PhoneNumberAddDTO phoneNumberAddDTO);

  void addEmail(@NotNull @Valid EmailAddDTO emailAddDTO);

  void removePhoneNumber(@NotNull @Positive PhoneNumberRemoveDTO phoneNumberRemoveDTO);

  void removeEmail(@NotNull @Positive EmailRemoveDTO emailRemoveDTO);
}


