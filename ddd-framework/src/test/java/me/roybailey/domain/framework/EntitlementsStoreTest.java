package me.roybailey.domain.framework;

import me.roybailey.domain.DomainTestContainerBase;
import me.roybailey.domain.DomainUtils;
import me.roybailey.domain.ResultStatus;
import me.roybailey.domain.auth.api.EntitlementStore;
import me.roybailey.domain.auth.model.Entitlement;
import me.roybailey.domain.container.Neo4jTestContainer;
import me.roybailey.domain.container.PostgresTestContainer;
import me.roybailey.domain.entitlements.store.Neo4jEntitlementStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.Driver;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;


@SpringBootTest
@ActiveProfiles("test")
public class EntitlementsStoreTest extends DomainTestContainerBase {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public Driver neo4j;

    private EntitlementStore entitlementStore;

    @BeforeEach
    public void setUp() {
        this.entitlementStore = new Neo4jEntitlementStore(neo4j);
    }

    @Test
    @Order(10)
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

    @Test
    @Order(20)
    public void testEntitlementsStoreRead() {
        var results = entitlementStore.findEntitlements();
        logger.info(DomainUtils.multiline("Entitlements found\n", results.getData()));
        assertThat(results.getStatus()).isEqualTo(ResultStatus.OK);
        assertThat(results.getData()).isNotNull();
        assertThat(results.getMessage()).isNotNull();
    }
}
