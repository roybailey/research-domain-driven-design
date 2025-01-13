package me.roybailey.domain.audit;

import lombok.extern.slf4j.Slf4j;
import me.roybailey.domain.DomainResult;
import me.roybailey.domain.DomainTestContainerBase;
import me.roybailey.domain.DomainUtils;
import me.roybailey.domain.ResultStatus;
import me.roybailey.domain.audit.api.AuditStore;
import me.roybailey.domain.audit.model.AuditEventAction;
import me.roybailey.domain.audit.model.AuditEventRecord;
import me.roybailey.domain.audit.model.AuditEventType;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class AuditStoreTest extends DomainTestContainerBase {

    @Autowired
    public DSLContext jooq;

    private AuditStore auditStore;

    private List<AuditEventRecord> listAuditEvents = List.of(
            AuditEventRecord.builder().type(AuditEventType.ENTITLEMENT).action(AuditEventAction.CREATE).reference("E1").build(),
            AuditEventRecord.builder().type(AuditEventType.ENTITLEMENT).action(AuditEventAction.UPDATE).reference("E1").build(),
            AuditEventRecord.builder().type(AuditEventType.ENTITLEMENT).action(AuditEventAction.DELETE).reference("E1").build(),
            AuditEventRecord.builder().type(AuditEventType.GROUP).action(AuditEventAction.CREATE).reference("G1").build(),
            AuditEventRecord.builder().type(AuditEventType.GROUP).action(AuditEventAction.UPDATE).reference("G1").build(),
            AuditEventRecord.builder().type(AuditEventType.GROUP).action(AuditEventAction.DELETE).reference("G1").build(),
            AuditEventRecord.builder().type(AuditEventType.PACKAGE).action(AuditEventAction.CREATE).reference("P1").build(),
            AuditEventRecord.builder().type(AuditEventType.PACKAGE).action(AuditEventAction.UPDATE).reference("P1").build(),
            AuditEventRecord.builder().type(AuditEventType.PACKAGE).action(AuditEventAction.DELETE).reference("P1").build()
    );
    private int initialAuditCount = 0;

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
        log.info("results: {}", results);
        assertThat(results.size()).isEqualTo(listAuditEvents.size());
        // number of inserted rows should match original list size
        assertThat(results.stream().map(DomainResult::getData).reduce(0L, Long::sum)).isEqualTo(listAuditEvents.size());
    }

    @Test
    @Order(20)
    public void testAuditStoreRead() {
        var results = auditStore.loadEvents();
        log.info(DomainUtils.multiline("Audit Events found\n", results.getData()));
        assertThat(results.getStatus()).isEqualTo(ResultStatus.OK);
        assertThat(results.getData()).isNotNull();
        assertThat(results.getMessage()).isNotNull();

        var saved = results.getData();
        var matched = saved.stream().filter(savedEvent -> {
            log.info(savedEvent.toString());
            assertThat(savedEvent.getId()).isNotNull();
            savedEvent.setId(null);
            return listAuditEvents.contains(savedEvent);
        }).toList();
        assertThat(matched.size()).isEqualTo(listAuditEvents.size());
    }
}
