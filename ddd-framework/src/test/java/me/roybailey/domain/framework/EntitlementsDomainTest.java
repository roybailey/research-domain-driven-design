package me.roybailey.domain.framework;

import me.roybailey.domain.DomainTestContainerBase;
import me.roybailey.domain.ResultStatus;
import me.roybailey.domain.audit.AuditDomainService;
import me.roybailey.domain.audit.api.AuditDomain;
import me.roybailey.domain.audit.api.AuditStore;
import me.roybailey.domain.audit.store.PostgresAuditStore;
import me.roybailey.domain.auth.api.EntitlementDomain;
import me.roybailey.domain.auth.api.EntitlementStore;
import me.roybailey.domain.auth.model.Entitlement;
import me.roybailey.domain.entitlements.EntitlementsDomainService;
import me.roybailey.domain.entitlements.store.Neo4jEntitlementStore;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
public class EntitlementsDomainTest extends DomainTestContainerBase {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public Driver neo4j;

    @Autowired
    public DSLContext jooq;

    private AuditStore auditStore;
    private AuditDomain auditDomain;
    private EntitlementStore entitlementStore;
    private EntitlementDomain entitlementDomain;

    @BeforeEach
    public void setUp() {
        this.auditStore = new PostgresAuditStore(jooq);
        this.auditDomain = new AuditDomainService(auditStore);
        this.entitlementStore = new Neo4jEntitlementStore(neo4j);
        this.entitlementDomain = new EntitlementsDomainService(auditDomain, entitlementStore);
    }

    @Test
    public void testEntitlementsStoreCreate() {

        var result = entitlementDomain.createEntitlements(List.of(
                Entitlement.builder().name("A").description("AAA").build(),
                Entitlement.builder().name("B").description("BBB").build(),
                Entitlement.builder().name("C").description("CCC").build()
        ));
        logger.info("result: {}", result);
        assertThat(result.getStatus()).isEqualTo(ResultStatus.OK);
        assertThat(result.getData()).isNotNull();
        assertThat(result.getMessage()).isNotNull();
    }
}
