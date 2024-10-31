package me.roybailey.domain.service;

import me.roybailey.domain.common.container.Neo4jTestContainer;
import me.roybailey.domain.common.container.PostgresTestContainer;
import me.roybailey.domain.service.audit.AuditPostgresStorage;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
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
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;


@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
public class DomainTestContainerTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

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
    public static final void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresDatabase::getJdbcUrl);
        registry.add("spring.datasource.password", postgresDatabase::getPassword);
        registry.add("spring.datasource.username", postgresDatabase::getUsername);
        registry.add("neo4j.url", neo4jDatabase::getBoltUrl);
    }

    @Autowired
    public DSLContext jooq;

    @Autowired
    public Driver neo4j;

    @Test
    public void testDomainContextInitialised() {
    }

    @Test
    public void testPostgresTestContainerInitialized() {

        var auditSql = jooq.select().from(AuditPostgresStorage.AUDIT);
        var auditData = auditSql.fetch();

        assertThat(auditData.size()).isEqualTo(5);
    }


    @Test
    public void testNeo4jTestContainerInitialized() {

        try (Session session = neo4j.session()) {
            Long count = session.run("MATCH (n) return count(n)", Collections.emptyMap()).next().get(0).asLong();
            logger.info("Node count "+count);
            assertThat(count).isEqualTo(1);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertThat(neo4jDatabase).isNotNull();
    }
}
