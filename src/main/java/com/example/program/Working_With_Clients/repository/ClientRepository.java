package com.example.program.Working_With_Clients.repository;

import com.example.program.Working_With_Clients.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
