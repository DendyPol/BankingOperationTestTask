package ru.polovinko.bankingservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.polovinko.bankingservice.entity.User;
import ru.polovinko.bankingservice.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class DefaultUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var user = userRepository.findByUsername(username)
      .orElseThrow(() -> new UsernameNotFoundException("User not found. Username is: " + username));
    return new AppUserDetails(user);
  }
}
