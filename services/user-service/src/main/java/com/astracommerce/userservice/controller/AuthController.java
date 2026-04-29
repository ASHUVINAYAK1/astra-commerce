package com.astracommerce.userservice.controller;

import com.astracommerce.userservice.dto.AuthResponse;
import com.astracommerce.userservice.dto.LoginRequest;
import com.astracommerce.userservice.dto.UserRequest;
import com.astracommerce.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Auth Controller — handles user registration and login.
 * These endpoints are publicly accessible (no JWT required).
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * POST /auth/register
     * Register a new user. Returns a JWT on success.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(request));
    }

    /**
     * POST /auth/login
     * Authenticate and receive a JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }
}
