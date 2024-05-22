package ru.polovinko.bankingservice.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.polovinko.bankingservice.exception.AlreadyExistException;
import ru.polovinko.bankingservice.repository.EmailRepository;
import ru.polovinko.bankingservice.repository.PhoneNumberRepository;
import ru.polovinko.bankingservice.repository.UserRepository;
import ru.polovinko.bankingservice.security.service.DefaultSecurityService;
import ru.polovinko.bankingservice.web.model.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
  private final UserRepository userRepository;
  private final EmailRepository emailRepository;
  private final PhoneNumberRepository phoneNumberRepository;
  private final DefaultSecurityService defaultSecurityService;

  @PostMapping("/signing")
  public ResponseEntity<AuthResponse> authUser(@RequestBody @Valid LoginRequest loginRequest) {
    return ResponseEntity.ok(defaultSecurityService.authenticationUser(loginRequest));
  }

  @PostMapping("/register")
  public ResponseEntity<SimpleResponse> registerUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
    if (userRepository.existsByUsername(createUserRequest.getUsername())) {
      throw new AlreadyExistException("Username already exists");
    }
    if (emailRepository.existsByEmail(createUserRequest.getEmail())) {
      throw new AlreadyExistException("Email already exists");
    }
    if (phoneNumberRepository.existsByPhoneNumber(createUserRequest.getPhone())) {
      throw new AlreadyExistException("Phone already exists");
    }
    defaultSecurityService.register(createUserRequest);
    return ResponseEntity.ok(new SimpleResponse("user created!"));
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
    return ResponseEntity.ok(defaultSecurityService.refreshToken(request));
  }

  @PostMapping("/logout")
  public ResponseEntity<SimpleResponse> logoutUser(@AuthenticationPrincipal UserDetails userDetails) {
    defaultSecurityService.logout();
    return ResponseEntity.ok(new SimpleResponse("User logout. Username is: " + userDetails.getUsername()));
  }
}
