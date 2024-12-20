package com.example.program.Working_With_Clients.controller;

import com.example.program.Working_With_Clients.ennum.Role;
import com.example.program.Working_With_Clients.entity.User;
import com.example.program.Working_With_Clients.repository.UserRepository;
import com.example.program.Working_With_Clients.security.AuthenticationRequest;
import com.example.program.Working_With_Clients.security.JwtTokenProvider;
import com.example.program.Working_With_Clients.security.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "Регистрация нового пользователя", description = "Регистрирует нового пользователя с указанным именем пользователя и паролем.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован"),
            @ApiResponse(responseCode = "400", description = "Имя пользователя уже существует")
    })
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Parameter(description = "Запрос на регистрацию, содержащий имя пользователя, пароль и роль ")
                                               @RequestBody RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Имя пользователя существует!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Назначение роли пользователя
        if ("ADMIN".equalsIgnoreCase(request.getRole())) {
            user.setRoles(Collections.singleton(Role.ADMIN));
        } else {
            user.setRoles(Collections.singleton(Role.USER));
        }

        userRepository.save(user);
        return ResponseEntity.ok("Пользователь успешно зарегистрирован!");
    }

    @Operation(summary = "Аутентификация пользователя", description = "Аутентифицирует пользователя и возвращает токен JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно вошел в систему и токен возвращен"),
            @ApiResponse(responseCode = "401", description = "Неверное имя пользователя или пароль")
    })
    @PostMapping("/authentication")
    public ResponseEntity<String> login(@Parameter(description = "Запрос на вход, содержащий имя пользователя и пароль")
                                        @RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            Authentication authentication =  authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
            if (authentication.isAuthenticated()) {
                System.out.println("Аутентификация успешна для пользователя: " + authenticationRequest.getUsername());
                String token = jwtTokenProvider.generateToken(authenticationRequest.getUsername());
                System.out.println("Generated token: " + token);
                return ResponseEntity.ok(token);
            } else {
                throw new Exception("Недействительные учетные данные");
            }
        } catch (AuthenticationException e) {
            e.printStackTrace();
            throw new Exception("Недействительные учетные данные", e);
        }

    }

}
