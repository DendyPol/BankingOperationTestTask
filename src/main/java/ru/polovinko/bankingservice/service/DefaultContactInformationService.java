package ru.polovinko.bankingservice.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.polovinko.bankingservice.dto.EmailAddDTO;
import ru.polovinko.bankingservice.dto.EmailRemoveDTO;
import ru.polovinko.bankingservice.dto.PhoneNumberAddDTO;
import ru.polovinko.bankingservice.dto.PhoneNumberRemoveDTO;
import ru.polovinko.bankingservice.entity.Email;
import ru.polovinko.bankingservice.entity.PhoneNumber;
import ru.polovinko.bankingservice.exception.EntityNotFoundException;
import ru.polovinko.bankingservice.repository.EmailRepository;
import ru.polovinko.bankingservice.repository.PhoneNumberRepository;
import ru.polovinko.bankingservice.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class DefaultContactInformationService implements ContactInformationService {
  private final PhoneNumberRepository phoneNumberRepository;
  private final EmailRepository emailRepository;
  private final UserRepository userRepository;
  private final ModelMapper modelMapper;

  public void addPhoneNumber(@NotNull @Valid PhoneNumberAddDTO dto) {
    var user = userRepository.findById(dto.getUserId())
      .orElseThrow(() -> new EntityNotFoundException("User not found!"));
    if (phoneNumberRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
      throw new IllegalArgumentException("Phone number already exists!");
    }
    var newPhoneNumber = modelMapper.map(dto, PhoneNumber.class);
    newPhoneNumber.setUser(user);
    phoneNumberRepository.save(newPhoneNumber);
  }

  public void addEmail(@NotNull @Valid EmailAddDTO dto) {
    var user = userRepository.findById(dto.getUserId())
      .orElseThrow(() -> new EntityNotFoundException("User not found!"));
    if (emailRepository.existsByEmail(dto.getEmail())) {
      throw new IllegalArgumentException("Email already exists!");
    }
    var newEmail = modelMapper.map(dto, Email.class);
    newEmail.setUser(user);
    emailRepository.save(newEmail);
  }

  public void removePhoneNumber(@NotNull @Positive PhoneNumberRemoveDTO dto) {
    var user = userRepository.findById(dto.getUserId())
      .orElseThrow(() -> new EntityNotFoundException("User not found!"));
    if (user.getPhoneNumbers().size() <= 1) {
      throw new IllegalArgumentException("Cannot remove the last phone number!");
    }
    var phoneNumber = phoneNumberRepository.findById(dto.getPhoneNumberId())
      .orElseThrow(() -> new EntityNotFoundException("Phone number not found!"));
    phoneNumberRepository.delete(phoneNumber);
  }

  public void removeEmail(@NotNull @Positive EmailRemoveDTO emailRemoveDTO) {
    var user = userRepository.findById(emailRemoveDTO.getUserId())
      .orElseThrow(() -> new EntityNotFoundException("User not found!"));
    if (user.getEmails().size() <= 1) {
      throw new IllegalArgumentException("Cannot remove the last email!");
    }
    var email = emailRepository.findById(emailRemoveDTO.getEmailId())
      .orElseThrow(() -> new EntityNotFoundException("Email not found!"));
    emailRepository.delete(email);
  }
}
