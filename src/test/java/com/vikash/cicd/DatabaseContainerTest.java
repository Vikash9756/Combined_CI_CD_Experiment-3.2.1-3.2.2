package com.vikash.cicd;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
 * Demonstrates infrastructure testing with a real temporary PostgreSQL container.
 * Docker must be running for this test. The application demo itself uses in-memory storage.
 */
@Testcontainers(disabledWithoutDocker = true)
class DatabaseContainerTest {
    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("experimentdb")
                    .withUsername("student")
                    .withPassword("studentpass");

    @Test
    void postgresContainerStartsAndAcceptsJdbcUrl() {
        assertTrue(postgres.isRunning());
        assertTrue(postgres.getJdbcUrl().startsWith("jdbc:postgresql:"));
    }
}
