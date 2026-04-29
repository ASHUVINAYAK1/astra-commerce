package com.astracommerce.userservice.service;

import com.astracommerce.userservice.dto.AuthResponse;
import com.astracommerce.userservice.dto.LoginRequest;
import com.astracommerce.userservice.dto.UserRequest;
import com.astracommerce.userservice.dto.UserResponse;
import com.astracommerce.userservice.entity.Role;
import com.astracommerce.userservice.entity.User;
import com.astracommerce.userservice.exception.BusinessException;
import com.astracommerce.userservice.exception.ResourceNotFoundException;
import com.astracommerce.userservice.repository.UserRepository;
import com.astracommerce.userservice.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * UserServiceImpl — concrete implementation of UserService.
 *
 * Layer rules:
 * - Only accesses data via UserRepository
 * - Maps entities to DTOs before returning (never exposes entities)
 * - Handles all business validation
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthResponse register(UserRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email '" + request.getEmail() + "' is already registered");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        User saved = userRepository.save(user);
        log.info("User registered successfully with id: {}", saved.getId());

        String token = jwtService.generateToken(saved.getEmail(), saved.getRole().name());
        return buildAuthResponse(token, saved);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Failed login attempt for email: {}", request.getEmail());
            throw new BadCredentialsException("Invalid email or password");
        }

        log.info("User logged in successfully: {}", user.getId());
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
        return buildAuthResponse(token, user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        log.debug("Fetching user by id: {}", id);
        User user = findUserOrThrow(id);
        return toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        log.debug("Fetching all users");
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest request) {
        log.info("Updating user id: {}", id);
        User user = findUserOrThrow(id);

        // If email is being changed, check it's not taken by someone else
        if (!user.getEmail().equals(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email '" + request.getEmail() + "' is already in use");
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        User updated = userRepository.save(user);
        log.info("User updated successfully: {}", updated.getId());
        return toResponse(updated);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user id: {}", id);
        User user = findUserOrThrow(id);
        userRepository.delete(user);
        log.info("User deleted: {}", id);
    }

    // -----------------------------------------------------------------------
    // Private helpers
    // -----------------------------------------------------------------------

    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }

    private AuthResponse buildAuthResponse(String token, User user) {
        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole().name())
                .expiresIn(86400L)   // 24 hours
                .build();
    }
}
