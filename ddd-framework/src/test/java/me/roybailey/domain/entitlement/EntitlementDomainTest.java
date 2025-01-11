package me.roybailey.domain.entitlement;

import lombok.extern.slf4j.Slf4j;
import me.roybailey.domain.DomainTestContainerBase;
import me.roybailey.domain.ResultStatus;
import me.roybailey.domain.audit.AuditDomainService;
import me.roybailey.domain.audit.PostgresAuditStore;
import me.roybailey.domain.audit.api.AuditDomain;
import me.roybailey.domain.audit.api.AuditStore;
import me.roybailey.domain.entitlement.api.EntitlementDomain;
import me.roybailey.domain.entitlement.api.EntitlementStore;
import me.roybailey.domain.entitlement.model.Entitlement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class EntitlementDomainTest extends DomainTestContainerBase {

    private AuditStore auditStore;
    private AuditDomain auditDomain;
    private EntitlementStore entitlementStore;
    private EntitlementDomain entitlementDomain;

    private List<Entitlement> listEntitlements = List.of(
            Entitlement.builder().name("A").description("AAA").build(),
            Entitlement.builder().name("B").description("BBB").build(),
            Entitlement.builder().name("C").description("CCC").build()
    );

    @BeforeAll
    public void setUp() {
        this.auditStore = new PostgresAuditStore(jooq);
        this.auditDomain = new AuditDomainService(auditStore);
        this.entitlementStore = new Neo4jEntitlementStore(neo4j);
        this.entitlementDomain = new EntitlementDomainService(auditDomain, entitlementStore);
    }

    @Test
    @Order(10)
    public void testEntitlementsCreation() {

        var results = entitlementDomain.createEntitlements(listEntitlements);
        log.info("result: {}", results);
        assertThat(results.getStatus()).isEqualTo(ResultStatus.OK);
        assertThat(results.getData()).isNotNull();
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

        var stats = entitlementDomain.getEntitlementsStats();
        log.info("stats: {}", stats);
        assertThat(stats.getStatus()).isEqualTo(ResultStatus.OK);
        assertThat(stats.getData()).isNotNull();
        // number of inserted rows should match original list size
        assertThat(stats.getData().get("totalEntitlements")).isEqualTo(3L);

        var events = auditStore.loadEvents();
        assertThat(events.getStatus()).isEqualTo(ResultStatus.OK);
        assertThat(events.getData()).isNotNull();
        assertThat(events.getData().size()).isGreaterThanOrEqualTo(listEntitlements.size());

    }

    @Test
    @Order(20)
    public void testEntitlementsRead() {

        var results = entitlementDomain.getEntitlements(listEntitlements.stream().map(Entitlement::getId).toList());
        log.info("result: {}", results);
        assertThat(results.getStatus()).isEqualTo(ResultStatus.OK);
        assertThat(results.getData()).isNotNull();
        // number of inserted rows should match original list size
        assertThat(results.getData().size()).isEqualTo(listEntitlements.size());
        // each entitlement inserted should match original list entry but with id populated
        results.getData().forEach( saved -> {
            assertThat(saved).isNotNull();
            assertThat(saved.getId()).isNotNull();
            assertThat(saved.getName()).isNotNull();
            assertThat(saved.getDescription()).isNotNull();
            assertThat(listEntitlements).contains(saved);
        });

        var events = auditStore.loadEvents();
        assertThat(events.getStatus()).isEqualTo(ResultStatus.OK);
        assertThat(events.getData()).isNotNull();
        assertThat(events.getData().size()).isGreaterThanOrEqualTo(listEntitlements.size());
    }


    @Test
    @Order(40)
    public void testEntitlementsDelete() {

        var results = entitlementDomain.deleteEntitlements(listEntitlements.stream().map(Entitlement::getId).toList());
        log.info("result: {}", results);
        assertThat(results.getStatus()).isEqualTo(ResultStatus.OK);
        assertThat(results.getData()).isNotNull();
        // number of inserted rows should match original list size
        assertThat(results.getData()).isEqualTo(3L);

        var stats = entitlementDomain.getEntitlementsStats();
        log.info("stats: {}", stats);
        assertThat(stats.getStatus()).isEqualTo(ResultStatus.OK);
        assertThat(stats.getData()).isNotNull();
        // number of inserted rows should match original list size
        assertThat(stats.getData().get("totalEntitlements")).isEqualTo(0L);

        var events = auditStore.loadEvents();
        assertThat(events.getStatus()).isEqualTo(ResultStatus.OK);
        assertThat(events.getData()).isNotNull();
        assertThat(events.getData().size()).isGreaterThanOrEqualTo(listEntitlements.size());

    }

}
