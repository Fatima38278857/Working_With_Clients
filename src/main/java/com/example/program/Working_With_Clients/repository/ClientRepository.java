package com.example.program.Working_With_Clients.repository;

import com.example.program.Working_With_Clients.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface  ClientRepository extends JpaRepository<Client, Long> {


    //  Объединение данных: Клиенты и администраторы
    @Query("SELECT c.id AS clientId, c.name AS clientName, u.username AS createdByAdmin " +
            "FROM Client c JOIN c.createdBy u " +
            "WHERE 'ADMIN' MEMBER OF u.roles")
    List<Object[]> getClientsCreatedByAdmins();

    //  Поиск дубликатов email
    @Query("SELECT c.email, COUNT(c) AS duplicateCount " +
            "FROM Client c " +
            "GROUP BY c.email " +
            "HAVING COUNT(c) > 1")
    List<Object[]> findDuplicateEmails();
}
