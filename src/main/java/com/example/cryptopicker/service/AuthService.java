package com.example.cryptopicker.service;

import com.example.cryptopicker.exception.RegistrationException;
import com.example.cryptopicker.model.User;
import com.example.cryptopicker.model.UserRole;
import com.example.cryptopicker.model.dto.AuthResponse;
import com.example.cryptopicker.model.dto.LoginRequest;
import com.example.cryptopicker.model.dto.RegisterRequest;
import com.example.cryptopicker.repository.UserRepository;
import com.example.cryptopicker.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthService(UserRepository userRepository, 
                      PasswordEncoder passwordEncoder,
                      JwtService jwtService,
                      AuthenticationManager authenticationManager,
                      TokenBlacklistService tokenBlacklistService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    public AuthResponse register(RegisterRequest request) {
        // Validate email format
        if (!isValidEmail(request.getEmail())) {
            throw new RegistrationException("Invalid email format");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RegistrationException("Email already registered");
        }

        // Validate password
        if (request.getPassword().length() < 6) {
            throw new RegistrationException("Password must be at least 6 characters long");
        }

        // Create new user
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .role(UserRole.ROLE_USER)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
        
        // Generate JWT token
        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(token, user.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // Update last login time
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);

            String token = jwtService.generateToken(user.getEmail());
            return new AuthResponse(token, user.getEmail());
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid email or password");
        }
    }

    public void logout(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        tokenBlacklistService.blacklistToken(token);
        SecurityContextHolder.clearContext();
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UsernameNotFoundException("No authenticated user found");
        }
        
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}