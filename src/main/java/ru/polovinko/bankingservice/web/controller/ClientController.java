package ru.polovinko.bankingservice.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.polovinko.bankingservice.dto.ClientDTO;
import ru.polovinko.bankingservice.dto.SearchCriteriaDTO;
import ru.polovinko.bankingservice.service.ClientService;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {
  private final ClientService clientService;

  @GetMapping("/search")
  public Page<ClientDTO> searchClient(SearchCriteriaDTO searchCriteriaDTO, Pageable pageable) {
    return clientService.searchClients(searchCriteriaDTO, pageable);
  }
}
