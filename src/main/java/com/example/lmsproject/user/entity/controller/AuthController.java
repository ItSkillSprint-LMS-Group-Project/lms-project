package com.example.lmsproject.user.entity.controller;

import com.example.lmsproject.user.entity.dto.response.AuthResponse;
import com.example.lmsproject.user.entity.dto.request.LoginRequest;
import com.example.lmsproject.user.entity.dto.request.RegisterRequest;
import com.example.lmsproject.user.entity.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}