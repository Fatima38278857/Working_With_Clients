package com.example.program.Working_With_Clients.repository;

import com.example.program.Working_With_Clients.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<Object> findByUsername(String username);
}
