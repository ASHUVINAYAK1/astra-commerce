package com.astracommerce.userservice.service;

import com.astracommerce.userservice.dto.AuthResponse;
import com.astracommerce.userservice.dto.LoginRequest;
import com.astracommerce.userservice.dto.UserRequest;
import com.astracommerce.userservice.dto.UserResponse;

import java.util.List;

/**
 * User Service contract.
 * All business logic for user management lives behind this interface.
 * Controllers only talk to this interface — never to the repository directly.
 */
public interface UserService {

    /**
     * Register a new user (ROLE_USER by default).
     * Hashes the password before persistence.
     * Throws BusinessException if email already exists.
     */
    AuthResponse register(UserRequest request);

    /**
     * Authenticate a user and return a signed JWT.
     * Throws BadCredentialsException on failure.
     */
    AuthResponse login(LoginRequest request);

    /**
     * Retrieve a user's public profile by ID.
     * Throws ResourceNotFoundException if not found.
     */
    UserResponse getUserById(Long id);

    /**
     * Retrieve all users (admin-only operation).
     */
    List<UserResponse> getAllUsers();

    /**
     * Update a user's profile.
     * Throws ResourceNotFoundException if not found.
     */
    UserResponse updateUser(Long id, UserRequest request);

    /**
     * Delete a user by ID.
     * Throws ResourceNotFoundException if not found.
     */
    void deleteUser(Long id);
}
