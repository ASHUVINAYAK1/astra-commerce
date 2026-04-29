package com.astracommerce.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * AstraCommerce User Service
 *
 * Responsible for:
 * - User registration and profile management
 * - JWT-based authentication (login/register)
 * - Role-based access control
 *
 * Registers with Eureka Discovery Server on startup.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
