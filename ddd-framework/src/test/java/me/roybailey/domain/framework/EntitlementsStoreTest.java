package me.roybailey.domain.framework;

import me.roybailey.domain.DomainResult;
import me.roybailey.domain.ResultStatus;
import me.roybailey.domain.auth.api.EntitlementStore;
import me.roybailey.domain.auth.model.Entitlement;
import me.roybailey.domain.container.Neo4jTestContainer;
import me.roybailey.domain.container.PostgresTestContainer;
import me.roybailey.domain.entitlements.store.Neo4jEntitlementStore;
import org.assertj.core.api.Assertions;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.List;

import static me.roybailey.domain.audit.store.AuditPostgresStorage.AUDIT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;


@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
public class EntitlementsStoreTest {

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

    @Autowired
    public Driver neo4j;

    private EntitlementStore entitlementStore;

    @BeforeEach
    public void setUp() {
        this.entitlementStore = new Neo4jEntitlementStore(neo4j);
    }

    @Test
    public void testDomainContextInitialised() {
    }

    @Test
    public void testEntitlementsStoreCreate() {

        var result = entitlementStore.saveEntitlements(List.of(
                Entitlement.builder().id("1").name("A").description("AAA").build(),
                Entitlement.builder().id("2").name("B").description("BBB").build(),
                Entitlement.builder().id("3").name("C").description("CCC").build()
        ));
        logger.info("result: {}", result);
        assertThat(result.getStatus()).isEqualTo(ResultStatus.OK);
        assertThat(result.getData()).isNotNull();
        assertThat(result.getMessage()).isNotNull();
    }
}
