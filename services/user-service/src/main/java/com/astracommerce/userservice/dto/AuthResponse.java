package com.astracommerce.userservice.dto;

import lombok.Builder;
import lombok.Data;

/**
 * JWT authentication response returned after successful login/register.
 */
@Data
@Builder
public class AuthResponse {

    private String token;
    private String type;        // "Bearer"
    private Long userId;
    private String email;
    private String role;
    private long expiresIn;     // seconds until expiry
}
