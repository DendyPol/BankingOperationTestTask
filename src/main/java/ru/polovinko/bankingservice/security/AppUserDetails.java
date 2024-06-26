package ru.polovinko.bankingservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.polovinko.bankingservice.entity.BankAccount;
import ru.polovinko.bankingservice.entity.Email;
import ru.polovinko.bankingservice.entity.PhoneNumber;
import ru.polovinko.bankingservice.entity.User;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AppUserDetails implements UserDetails {

  private final User user;

  public Long getId() {
    return user.getId();
  }

  public Set<String> getPhones() {
    return user.getPhoneNumbers().stream()
      .map(PhoneNumber::getPhoneNumber)
      .collect(Collectors.toSet());
  }

  public Set<String> getEmails() {
    return user.getEmails().stream()
      .map(Email::getEmail)
      .collect(Collectors.toSet());
  }

  public BankAccount getBankAccount() {
    return user.getBankAccount();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return user.getRoles().stream()
      .map(roleType -> new SimpleGrantedAuthority(roleType.name()))
      .toList();
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
