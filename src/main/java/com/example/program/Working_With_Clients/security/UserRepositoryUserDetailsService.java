package com.example.program.Working_With_Clients.security;

import com.example.program.Working_With_Clients.entity.User;
import com.example.program.Working_With_Clients.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRepositoryUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = (User) userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User не найден: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // Пароль уже должен быть закодирован
                .roles(user.getRoles().stream().map(Enum::name).toArray(String[]::new))
                .build();
    }
}
