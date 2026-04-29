package com.astracommerce.userservice.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when a business rule is violated.
 * Example: registering with an already-existing email.
 * Maps to HTTP 409 Conflict via GlobalExceptionHandler.
 */
public class BusinessException extends RuntimeException {

    private final HttpStatus status;

    public BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public BusinessException(String message) {
        this(message, HttpStatus.CONFLICT);
    }

    public HttpStatus getStatus() {
        return status;
    }
}
