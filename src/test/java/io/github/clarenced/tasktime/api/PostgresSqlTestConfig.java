package io.github.clarenced.tasktime.api;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Instead of defining a container for each test class,
 * you can specify a test configuration class that will be applied to all test classes.
 */
@TestConfiguration
public class PostgresSqlTestConfig {


    /**
     *  The annotation @ServiceConnection is required to connect to the container.
     *  It creates a JdbcConnnectionDetails that allows Spring to connect to the container.
     *  More here: <a href="https://spring.io/blog/2023/06/19/spring-boot-31-connectiondetails-abstraction">More here</a>
     */
    @Bean
    @ServiceConnection
    public  PostgreSQLContainer<?> createPostgresSqlContainer() {
        return new PostgreSQLContainer<>("postgres:16-alpine")
                 .withDatabaseName("taskTime")
                 .withUsername("test")
                 .withPassword("test");
        }
}
