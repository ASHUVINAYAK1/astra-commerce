package com.astracommerce.userservice.controller;

import com.astracommerce.userservice.dto.UserRequest;
import com.astracommerce.userservice.dto.UserResponse;
import com.astracommerce.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User Controller — CRUD operations on user profiles.
 *
 * Access rules:
 * - GET /users/{id}    → authenticated (own profile or admin)
 * - GET /users         → ADMIN only
 * - PUT /users/{id}    → authenticated (own profile or admin)
 * - DELETE /users/{id} → ADMIN only
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * GET /users/{id}
     * Get a user's public profile. Authenticated users only.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /**
     * GET /users
     * Get all users. ADMIN only.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * PUT /users/{id}
     * Update a user's profile.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    /**
     * DELETE /users/{id}
     * Delete a user. ADMIN only.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
