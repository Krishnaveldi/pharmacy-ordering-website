package com.pharmacy.service.impl;

import com.pharmacy.dto.AuthResponse;
import com.pharmacy.dto.LoginRequest;
import com.pharmacy.dto.RegisterRequest;
import com.pharmacy.entity.Role;
import com.pharmacy.entity.User;
import com.pharmacy.repository.UserRepository;
import com.pharmacy.security.JwtUtils;
import com.pharmacy.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    public String register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Role userRole = Role.CUSTOMER;
        if (request.getRole() != null && request.getRole().equalsIgnoreCase("ADMIN")) {
            userRole = Role.ADMIN;
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .address(request.getAddress())
                .role(userRole)
                .build();

        userRepository.save(user);

        return "User registered successfully";
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        String token = jwtUtils.generateToken(user.getEmail());

        return new AuthResponse(token, user.getRole().name());
    }
}