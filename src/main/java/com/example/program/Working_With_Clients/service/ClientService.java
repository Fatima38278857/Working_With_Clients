package com.example.program.Working_With_Clients.service;

import com.example.program.Working_With_Clients.entity.Client;
import com.example.program.Working_With_Clients.entity.User;
import com.example.program.Working_With_Clients.repository.ClientRepository;
import com.example.program.Working_With_Clients.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;


    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();
            return (User) userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }
        throw new RuntimeException("Authentication failed");
    }

    @Transactional
    public Client saveClient(Client client) {
        // Установка текущего пользователя как создателя клиента
        User currentUser = getCurrentUser();
        client.setCreatedBy(currentUser);
        client.setCreatedAt(LocalDateTime.now());
        return clientRepository.save(client);
    }

    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }


    public Client updateClient(Long id, Client updatedClient) {
        return clientRepository.findById(id)
                .map(client -> {
                    client.setName(updatedClient.getName());
                    client.setEmail(updatedClient.getEmail());
                    return clientRepository.save(client);
                })
                .orElseThrow(() -> new RuntimeException("Клиент не найден"));
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    public List<Object[]> getClientsCreatedByAdmins() {
        return clientRepository.getClientsCreatedByAdmins();
    }


    public List<Object[]> findDuplicateEmails() {
        return clientRepository.findDuplicateEmails();
    }
}
