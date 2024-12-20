package com.example.program.Working_With_Clients.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClientDTO {

    private Long id; // Для возврата идентификатора при чтении данных (если нужно)

    @NotBlank(message = "Имя клиента не может быть пустым")
    private String name;

    @Email(message = "Email должен быть валидным")
    @NotBlank(message = "Email обязателен")
    private String email;
}
