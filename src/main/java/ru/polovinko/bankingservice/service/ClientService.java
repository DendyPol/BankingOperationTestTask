package ru.polovinko.bankingservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.polovinko.bankingservice.dto.ClientDTO;
import ru.polovinko.bankingservice.dto.SearchCriteriaDTO;

public interface ClientService {
  Page<ClientDTO> searchClients(SearchCriteriaDTO searchCriteriaDTO, Pageable pageable);
}
