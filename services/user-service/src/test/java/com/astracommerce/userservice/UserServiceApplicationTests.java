package com.astracommerce.userservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Smoke test — verifies the Spring application context loads correctly.
 * This is the minimum acceptance criteria for Milestone 1.
 */
@SpringBootTest
@ActiveProfiles("test")
class UserServiceApplicationTests {

    @Test
    void contextLoads() {
        // If the Spring context loads without errors, this test passes.
        // This validates: bean wiring, config loading, JPA setup.
    }
}
