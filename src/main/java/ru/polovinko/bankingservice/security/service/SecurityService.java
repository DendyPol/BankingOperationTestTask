package ru.polovinko.bankingservice.security.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ru.polovinko.bankingservice.web.model.*;

public interface SecurityService {
  AuthResponse authenticationUser(@NotNull @Valid LoginRequest loginRequest);

  void register(@NotNull @Valid CreateUserRequest createUserRequest);

  RefreshTokenResponse refreshToken(@NotNull @Valid RefreshTokenRequest request);

  void logout();
}
