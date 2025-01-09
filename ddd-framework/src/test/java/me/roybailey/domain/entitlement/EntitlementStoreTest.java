package me.roybailey.domain.entitlement;

import me.roybailey.domain.DomainResult;
import me.roybailey.domain.DomainTestContainerBase;
import me.roybailey.domain.DomainUtils;
import me.roybailey.domain.ResultStatus;
import me.roybailey.domain.entitlement.api.EntitlementStore;
import me.roybailey.domain.entitlement.model.Entitlement;
import me.roybailey.domain.entitlement.store.Neo4jEntitlementStore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;


@SpringBootTest
@ActiveProfiles("test")
public class EntitlementStoreTest extends DomainTestContainerBase {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public Driver neo4j;

    private EntitlementStore entitlementStore;

    private List<Entitlement> listEntitlements = List.of(
            Entitlement.builder().id("1").name("A").description("AAA").build(),
            Entitlement.builder().id("2").name("B").description("BBB").build(),
            Entitlement.builder().id("3").name("C").description("CCC").build()
    );

    @BeforeAll
    public void setUp() {
        this.entitlementStore = new Neo4jEntitlementStore(neo4j);
    }

    @Test
    @Order(10)
    public void testEntitlementStoreSave() {

        var results = entitlementStore.saveEntitlements(listEntitlements);
        logger.info("results: {}", results);
        assertThat(results.getStatus()).isEqualTo(ResultStatus.OK);
        assertThat(results.getData()).isNotNull();
        assertThat(results.getData().size()).isEqualTo(listEntitlements.size());
        // number of inserted rows should match original list size
        assertThat(results.getData().stream().map(DomainResult::getData).reduce(0L, Long::sum)).isEqualTo(listEntitlements.size());
        assertThat(results.getMessage()).isNotNull();
    }

    @Test
    @Order(20)
    public void testEntitlementStoreFind() {
        var results = entitlementStore.findEntitlements();
        logger.info(DomainUtils.multiline("Entitlements found\n", results.getData()));
        assertThat(results.getStatus()).isEqualTo(ResultStatus.OK);
        assertThat(results.getData()).isNotNull();
        assertThat(results.getData().size()).isEqualTo(listEntitlements.size());
        assertThat(results.getMessage()).isNotNull();

        var saved = results.getData();
        saved.stream().forEach(savedEntitlement -> {
            assertThat(listEntitlements.indexOf(savedEntitlement)).isGreaterThanOrEqualTo(0);
        });
    }
}
