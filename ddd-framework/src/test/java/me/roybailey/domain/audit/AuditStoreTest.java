package me.roybailey.domain.audit;

import me.roybailey.domain.DomainResult;
import me.roybailey.domain.DomainTestContainerBase;
import me.roybailey.domain.DomainUtils;
import me.roybailey.domain.ResultStatus;
import me.roybailey.domain.audit.api.AuditStore;
import me.roybailey.domain.audit.model.AuditEventAction;
import me.roybailey.domain.audit.model.AuditEventRecord;
import me.roybailey.domain.audit.model.AuditEventType;
import me.roybailey.domain.audit.store.PostgresAuditStore;
import me.roybailey.domain.entitlement.api.EntitlementStore;
import me.roybailey.domain.entitlement.model.Entitlement;
import me.roybailey.domain.entitlement.store.Neo4jEntitlementStore;
import org.jooq.DSLContext;
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


@SpringBootTest
@ActiveProfiles("test")
public class AuditStoreTest extends DomainTestContainerBase {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public DSLContext jooq;

    private AuditStore auditStore;

    private List<AuditEventRecord> listAuditEvents = List.of(
            AuditEventRecord.builder().id("1").type(AuditEventType.ENTITLEMENT).action(AuditEventAction.CREATE).reference("E1").build(),
            AuditEventRecord.builder().id("2").type(AuditEventType.ENTITLEMENT).action(AuditEventAction.UPDATE).reference("E1").build(),
            AuditEventRecord.builder().id("3").type(AuditEventType.ENTITLEMENT).action(AuditEventAction.DELETE).reference("E1").build(),
            AuditEventRecord.builder().id("4").type(AuditEventType.GROUP).action(AuditEventAction.CREATE).reference("G1").build(),
            AuditEventRecord.builder().id("5").type(AuditEventType.GROUP).action(AuditEventAction.UPDATE).reference("G1").build(),
            AuditEventRecord.builder().id("6").type(AuditEventType.GROUP).action(AuditEventAction.DELETE).reference("G1").build(),
            AuditEventRecord.builder().id("7").type(AuditEventType.PACKAGE).action(AuditEventAction.CREATE).reference("P1").build(),
            AuditEventRecord.builder().id("8").type(AuditEventType.PACKAGE).action(AuditEventAction.UPDATE).reference("P1").build(),
            AuditEventRecord.builder().id("9").type(AuditEventType.PACKAGE).action(AuditEventAction.DELETE).reference("P1").build()
    );

    @BeforeAll
    public void setUp() {
        this.auditStore = new PostgresAuditStore(jooq);
    }

    @Test
    @Order(10)
    public void testAuditStoreSave() {
        List<DomainResult<Long>> results = new java.util.ArrayList<>();
        listAuditEvents.forEach( event -> {
            results.add(auditStore.saveEvent(event));
        });
        logger.info("results: {}", results);
        assertThat(results.size()).isEqualTo(listAuditEvents.size());
        // number of inserted rows should match original list size
        assertThat(results.stream().map(DomainResult::getData).reduce(0L, Long::sum)).isEqualTo(listAuditEvents.size());
    }

    @Test
    @Order(20)
    public void testAuditStoreRead() {
        var results = auditStore.loadEvents();
        logger.info(DomainUtils.multiline("Audit Events found\n", results.getData()));
        assertThat(results.getStatus()).isEqualTo(ResultStatus.OK);
        assertThat(results.getData()).isNotNull();
        assertThat(results.getData().size()).isEqualTo(listAuditEvents.size());
        assertThat(results.getMessage()).isNotNull();

        var saved = results.getData();
        saved.forEach(savedEvent -> {
            logger.info(savedEvent.toString());
            assertThat(savedEvent.getId()).isNotNull();
            assertThat(listAuditEvents.indexOf(savedEvent)).isGreaterThanOrEqualTo(0);
        });
    }
}
