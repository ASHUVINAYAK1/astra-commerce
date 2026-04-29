package com.astracommerce.userservice.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standard API error response envelope.
 * All error responses from this service conform to this structure.
 *
 * {
 *   "timestamp": "...",
 *   "status": 404,
 *   "error": "Not Found",
 *   "message": "User not found with id: '5'",
 *   "path": "/users/5",
 *   "errors": []   // validation errors
 * }
 */
@Data
@Builder
public class ApiError {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<String> errors;   // field-level validation errors
}
