package me.roybailey.domain;

import lombok.extern.slf4j.Slf4j;
import me.roybailey.domain.container.Neo4jTestContainer;
import me.roybailey.domain.container.PostgresTestContainer;
import org.jooq.DSLContext;
import org.junit.jupiter.api.*;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collections;


@Slf4j
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@ActiveProfiles("test")
public class DomainTestContainerBase {

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
        registry.add("spring.neo4j.uri", neo4jDatabase::getBoltUrl);
        registry.add("neo4j.url", neo4jDatabase::getBoltUrl);
    }

    @Test
    @Order(-1)
    public void testDomainContextInitialised() {
    }

    @Autowired
    protected Driver neo4j;

    @Autowired
    protected DSLContext jooq;

    @AfterAll
    public void cleanNeo4jDatabase() {
        log.info("Deleting all Neo4j data {}", neo4j.hashCode());
        try (Session session = neo4j.session()) {
            var results = session.run("match (n) optional match (n)-[r]-() delete r,n", Collections.emptyMap());
        } catch (Exception e) {
            log.error("Error deleting all Neo4j data", e);
        }
    }
}
