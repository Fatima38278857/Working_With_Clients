package com.example.program.Working_With_Clients.controller;

import com.example.program.Working_With_Clients.dto.ClientDTO;
import com.example.program.Working_With_Clients.entity.Client;
import com.example.program.Working_With_Clients.mapper.ClientMapper;
import com.example.program.Working_With_Clients.repository.ClientRepository;
import com.example.program.Working_With_Clients.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final ClientMapper clientMapper;
    private final ClientRepository clientRepository;

    @GetMapping
    public List<ClientDTO> getAllClients() {
        return clientService.findAllClients()
                .stream()
                .map(clientMapper::toDTO)
                .toList();
    }

    @PostMapping("/add")
    public ClientDTO createClient(@Valid @RequestBody ClientDTO clientDTO) {
        Client client = clientMapper.toEntity(clientDTO);
        Client savedClient = clientService.saveClient(client);
        return clientMapper.toDTO(savedClient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable Long id, @Valid @RequestBody ClientDTO clientDTO) {
        Client client = clientMapper.toEntity(clientDTO);
        Client updatedClient = clientService.updateClient(id, client);
        return ResponseEntity.ok(clientMapper.toDTO(updatedClient));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/admins-clients")
    public List<Object[]> getClientsCreatedByAdmins() {
        return clientService.getClientsCreatedByAdmins();
    }


    @GetMapping("/duplicates")
    public List<Object[]> findDuplicateEmails() {
        return clientService.findDuplicateEmails();
    }
}
