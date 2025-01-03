package me.roybailey.domain;

import me.roybailey.domain.container.Neo4jTestContainer;
import me.roybailey.domain.container.PostgresTestContainer;
import org.assertj.core.api.Assertions;
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

import static me.roybailey.domain.audit.store.PostgresAuditStore.AUDIT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;


@SpringBootTest
@ActiveProfiles("test")
public class DomainTestContainerTest extends DomainTestContainerBase {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public DSLContext jooq;

    @Autowired
    public Driver neo4j;

    @Test
    public void testPostgresTestContainerInitialized() {

        var auditSql = jooq.select().from(AUDIT);
        var auditData = auditSql.fetch();

        Assertions.assertThat(auditData.size()).isGreaterThanOrEqualTo(5);
    }


    @Test
    public void testNeo4jTestContainerInitialized() {

        try (Session session = neo4j.session()) {
            Long count = session.run("MATCH (n) return count(n)", Collections.emptyMap()).next().get(0).asLong();
            logger.info("Node count {}", count);
            assertThat(count).isEqualTo(1);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertThat(neo4jDatabase).isNotNull();
    }
}
