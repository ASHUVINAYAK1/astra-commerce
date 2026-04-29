package com.astracommerce.userservice.dto;

import com.astracommerce.userservice.entity.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO for returning user data to API consumers.
 * Never includes the password — that stays server-side.
 */
@Data
@Builder
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private Role role;
    private LocalDateTime createdAt;
}
