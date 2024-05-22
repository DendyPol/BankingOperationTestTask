package ru.polovinko.bankingservice.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.polovinko.bankingservice.dto.EmailAddDTO;
import ru.polovinko.bankingservice.dto.EmailRemoveDTO;
import ru.polovinko.bankingservice.dto.PhoneNumberAddDTO;
import ru.polovinko.bankingservice.dto.PhoneNumberRemoveDTO;
import ru.polovinko.bankingservice.service.ContactInformationService;

@RestController
@RequestMapping("/api/v1/contact")
@RequiredArgsConstructor
public class ContactInformationController {
  private final ContactInformationService contactInformationService;

  @PostMapping("/addPhoneNumber")
  public ResponseEntity<String> addPhoneNumber(@RequestBody @Valid PhoneNumberAddDTO phoneNumberAddDTO) {
    contactInformationService.addPhoneNumber(phoneNumberAddDTO);
    return ResponseEntity.ok("Phone number removed successfully!");
  }

  @PostMapping("/addEmail")
  public ResponseEntity<String> addEmail(@RequestBody @Valid EmailAddDTO emailAddDTO) {
    contactInformationService.addEmail(emailAddDTO);
    return ResponseEntity.ok("Email added successfully!");
  }

  @DeleteMapping("/{userId}/phone/{phoneNumberId}")
  public ResponseEntity<String> deletePhoneNumber(@RequestBody PhoneNumberRemoveDTO phoneNumberRemoveDTO) {
    contactInformationService.removePhoneNumber(phoneNumberRemoveDTO);
    return ResponseEntity.ok("Phone number removed successfully!");
  }

  @DeleteMapping("/{userId}/email/{emailId}")
  public ResponseEntity<String> removeEmail(@RequestBody EmailRemoveDTO emailRemoveDTO) {
    contactInformationService.removeEmail(emailRemoveDTO);
    return ResponseEntity.ok("Email removed successfully");
  }
}
