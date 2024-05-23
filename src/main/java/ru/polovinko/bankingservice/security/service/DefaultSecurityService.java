package ru.polovinko.bankingservice.security.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.polovinko.bankingservice.entity.*;
import ru.polovinko.bankingservice.exception.RefreshTokenException;
import ru.polovinko.bankingservice.repository.BankAccountRepository;
import ru.polovinko.bankingservice.repository.EmailRepository;
import ru.polovinko.bankingservice.repository.PhoneNumberRepository;
import ru.polovinko.bankingservice.repository.UserRepository;
import ru.polovinko.bankingservice.security.AppUserDetails;
import ru.polovinko.bankingservice.security.jwt.JwtUtils;
import ru.polovinko.bankingservice.service.DefaultRefreshTokenService;
import ru.polovinko.bankingservice.web.model.*;

@Service
@RequiredArgsConstructor
public class DefaultSecurityService implements SecurityService {
  private final AuthenticationManager authenticationManager;
  private final JwtUtils jwtUtils;
  private final DefaultRefreshTokenService defaultRefreshTokenService;
  private final UserRepository userRepository;
  private final BankAccountRepository bankAccountRepository;
  private final PasswordEncoder passwordEncoder;
  private final PhoneNumberRepository phoneNumberRepository;
  private final EmailRepository emailRepository;
  private final ModelMapper modelMapper;

  @Override
  public AuthResponse authenticationUser(LoginRequest loginRequest) {
    var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
      loginRequest.getUsername(),
      loginRequest.getPassword()
    ));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    var userDetails = (AppUserDetails) authentication.getPrincipal();
    var roles = userDetails.getAuthorities().stream()
      .map(GrantedAuthority::getAuthority)
      .toList();
    var refreshToken = defaultRefreshTokenService.createRefreshToken(userDetails.getId());
    return AuthResponse.builder()
      .id(userDetails.getId())
      .token(jwtUtils.generateJwtToken(userDetails))
      .refreshToken(refreshToken.getToken())
      .username(userDetails.getUsername())
      .email(userDetails.getEmails())
      .phone(userDetails.getPhones())
      .bankAccount(userDetails.getBankAccount())
      .roles(roles)
      .build();
  }

  @Override
  public void register(CreateUserRequest createUserRequest) {
    var user = modelMapper.map(createUserRequest, User.class);
    user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
    user.setRoles(createUserRequest.getRoles());
    userRepository.save(user);
    var bank = BankAccount.builder()
      .user(user)
      .balance(createUserRequest.getBalance())
      .build();
    bankAccountRepository.save(bank);
    var phoneNumber = PhoneNumber.builder()
      .user(user)
      .phoneNumber(createUserRequest.getPhone())
      .build();
    phoneNumberRepository.save(phoneNumber);
    var email = Email.builder()
      .user(user)
      .email(createUserRequest.getEmail())
      .build();
    emailRepository.save(email);
  }

  @Override
  public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
    var requestRefreshToken = request.getRefreshToken();
    return defaultRefreshTokenService.findByRefreshToken(requestRefreshToken)
      .map(defaultRefreshTokenService::checkRefreshToken)
      .map(RefreshToken::getUserId)
      .map(userId -> {
        var tokenOwner = userRepository.findById(userId)
          .orElseThrow(() -> new RefreshTokenException("Exception trying to get token for userId: " + userId));
        var token = jwtUtils.generateTokenFromUsername(tokenOwner.getUsername());
        return new RefreshTokenResponse(token, defaultRefreshTokenService.createRefreshToken(userId).getToken());
      }).orElseThrow(() -> new RefreshTokenException(requestRefreshToken, "Refresh token not found"));
  }

  @Override
  public void logout() {
    var currentPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (currentPrincipal instanceof AppUserDetails userDetails) {
      Long userId = userDetails.getId();
      defaultRefreshTokenService.deleteByUserId(userId);
    }
  }
}
