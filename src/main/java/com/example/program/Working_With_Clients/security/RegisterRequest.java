package com.example.program.Working_With_Clients.security;

import lombok.Data;

@Data
public class RegisterRequest {

    private String username;
    private String password;
    private String role;
}
