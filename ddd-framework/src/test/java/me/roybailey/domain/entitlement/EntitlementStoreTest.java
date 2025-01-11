package me.roybailey.domain.entitlement;

import lombok.extern.slf4j.Slf4j;
import me.roybailey.domain.DomainTestContainerBase;
import me.roybailey.domain.DomainUtils;
import me.roybailey.domain.ResultStatus;
import me.roybailey.domain.entitlement.api.EntitlementStore;
import me.roybailey.domain.entitlement.model.Entitlement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class EntitlementStoreTest extends DomainTestContainerBase {

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
        log.info("results: {}", results);
        assertThat(results.getStatus()).isEqualTo(ResultStatus.OK);
        assertThat(results.getData()).isNotNull();
        assertThat(results.getData().size()).isEqualTo(listEntitlements.size());
        // number of inserted rows should match original list size
        assertThat(results.getData().size()).isEqualTo(listEntitlements.size());
        // each entitlement inserted should match original list entry but with id populated
        results.getData().forEach( savedEntitlementResult -> {
            assertThat(savedEntitlementResult.isSuccess()).isTrue();
            assertThat(savedEntitlementResult.getMessage()).isNotNull();
            var saved = savedEntitlementResult.getData();
            assertThat(saved).isNotNull();
            assertThat(saved.getId()).isNotNull();
            assertThat(saved.getName()).isNotNull();
            assertThat(saved.getDescription()).isNotNull();
            assertThat(listEntitlements).contains(savedEntitlementResult.getData());
        });

        assertThat(entitlementStore.getEntitlementStats().getData().get("totalEntitlements").toString()).isEqualTo("3");
    }

    @Test
    @Order(20)
    public void testEntitlementStoreFind() {
        var results = entitlementStore.findEntitlements(listEntitlements.stream().map(Entitlement::getId).toList());
        log.info(DomainUtils.multiline("Entitlements found\n", results.getData()));
        assertThat(results.getStatus()).isEqualTo(ResultStatus.OK);
        assertThat(results.getData()).isNotNull();
        assertThat(results.getData().size()).isEqualTo(listEntitlements.size());
        assertThat(results.getMessage()).isNotNull();

        var saved = results.getData();
        saved.forEach(savedEntitlement -> {
            assertThat(listEntitlements.indexOf(savedEntitlement)).isGreaterThanOrEqualTo(0);
        });
    }

    @Test
    @Order(40)
    public void testEntitlementStoreDelete() {
        var results = entitlementStore.deleteEntitlements(listEntitlements.stream().map(Entitlement::getId).toList());
        log.info("Entitlements found {}", results.getData());

        assertThat(results.getStatus()).isEqualTo(ResultStatus.OK);
        assertThat(results.getMessage()).isNotNull();
        assertThat(results.getData()).isNotNull();
        assertThat(results.getData()).isEqualTo(3L);

        assertThat(entitlementStore.getEntitlementStats().getData().get("totalEntitlements").toString()).isEqualTo("0");
    }
}
