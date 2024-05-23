package ru.polovinko.bankingservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.polovinko.bankingservice.dto.ClientDTO;
import ru.polovinko.bankingservice.dto.SearchCriteriaDTO;
import ru.polovinko.bankingservice.entity.User;
import ru.polovinko.bankingservice.repository.UserRepository;
import ru.polovinko.bankingservice.specification.UserSpecifications;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultClientService implements ClientService {
  private final UserRepository userRepository;

  @Override
  public Page<ClientDTO> searchClients(SearchCriteriaDTO searchCriteriaDTO, Pageable pageable) {
    var specification = Specification
      .where(UserSpecifications.dateOfBirthAfter(searchCriteriaDTO.getDateOfBirth()))
      .and(UserSpecifications.hasPhoneNumber(searchCriteriaDTO.getPhone()))
      .and(UserSpecifications.hasEmail(searchCriteriaDTO.getEmail()))
      .and(UserSpecifications.hasFullNameLike(searchCriteriaDTO.getFullName()));
    return userRepository.findAll(specification, pageable).map(this::toClientDTO);
  }

  private ClientDTO toClientDTO(User user) {
    return new ClientDTO(
      user.getId(),
      user.getUsername(),
      user.getEmails(),
      user.getPhoneNumbers(),
      user.getDateOfBirth(),
      user.getBankAccount().getBalance(),
      user.getRoles().stream().map(Enum::name).collect(Collectors.toSet())
    );
  }
}
