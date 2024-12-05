package com.example.cryptopicker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.cryptopicker.service.AuthService;
import com.example.cryptopicker.model.dto.*;
import jakarta.validation.Valid;
import com.example.cryptopicker.model.dto.RegisterRequest;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;
    
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        return ResponseEntity.ok().build();
    }
}