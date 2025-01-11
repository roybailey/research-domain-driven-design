package me.roybailey.domain.entitlement;

import me.roybailey.domain.DomainResult;
import me.roybailey.domain.DomainTestContainerBase;
import me.roybailey.domain.ResultStatus;
import me.roybailey.domain.audit.AuditDomainService;
import me.roybailey.domain.audit.api.AuditDomain;
import me.roybailey.domain.audit.api.AuditStore;
import me.roybailey.domain.audit.store.PostgresAuditStore;
import me.roybailey.domain.entitlement.api.EntitlementDomain;
import me.roybailey.domain.entitlement.api.EntitlementStore;
import me.roybailey.domain.entitlement.model.Entitlement;
import me.roybailey.domain.entitlement.store.Neo4jEntitlementStore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
public class EntitlementDomainTest extends DomainTestContainerBase {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
        logger.info("result: {}", results);
        assertThat(results.getStatus()).isEqualTo(ResultStatus.OK);
        assertThat(results.getData()).isNotNull();
        assertThat(results.getData().size()).isEqualTo(listEntitlements.size());
        // number of inserted rows should match original list size
        assertThat(results.getData().stream().map(DomainResult::getData).reduce(0L, Long::sum)).isEqualTo(listEntitlements.size());
        assertThat(results.getMessage()).isNotNull();

        var events = auditStore.loadEvents();
        assertThat(events.getStatus()).isEqualTo(ResultStatus.OK);
        assertThat(events.getData()).isNotNull();
        assertThat(events.getData().size()).isGreaterThanOrEqualTo(listEntitlements.size());
    }

}
