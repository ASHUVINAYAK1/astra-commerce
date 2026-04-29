package com.astracommerce.userservice.repository;

import com.astracommerce.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User repository — data access layer.
 * Extends JpaRepository for standard CRUD operations.
 *
 * Custom queries are defined here as needed.
 * Never accessed from outside the service layer.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by their email address.
     * Used during authentication to load user details.
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if an email is already registered.
     * Used during registration to prevent duplicates.
     */
    boolean existsByEmail(String email);
}
