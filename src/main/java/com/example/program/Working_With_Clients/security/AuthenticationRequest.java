package com.example.program.Working_With_Clients.security;


import lombok.Data;

@Data
public class AuthenticationRequest {
    private String username;
    private String password;
}
