package me.roybailey.domain;

import me.roybailey.domain.container.Neo4jTestContainer;
import me.roybailey.domain.container.PostgresTestContainer;
import org.assertj.core.api.Assertions;
import org.jooq.DSLContext;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collections;

import static me.roybailey.domain.audit.store.PostgresAuditStore.AUDIT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;


@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DomainTestContainerBase {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final PostgreSQLContainer<?> postgresDatabase = PostgresTestContainer.create(
            null,
            true,
            true
    );
    public static final Neo4jContainer<?> neo4jDatabase = Neo4jTestContainer.create(
            "password",
            "",
            true,
            true,
            true
    );

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresDatabase::getJdbcUrl);
        registry.add("spring.datasource.password", postgresDatabase::getPassword);
        registry.add("spring.datasource.username", postgresDatabase::getUsername);
        registry.add("neo4j.url", neo4jDatabase::getBoltUrl);
    }

    @Test
    public void testDomainContextInitialised() {
    }

}
