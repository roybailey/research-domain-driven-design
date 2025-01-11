package me.roybailey.domain.audit;

import lombok.extern.slf4j.Slf4j;
import me.roybailey.domain.DomainResult;
import me.roybailey.domain.DomainUtils;
import me.roybailey.domain.audit.api.AuditStore;
import me.roybailey.domain.audit.model.AuditEventAction;
import me.roybailey.domain.audit.model.AuditEventRecord;
import me.roybailey.domain.audit.model.AuditEventType;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;


@Slf4j
@Service
public class PostgresAuditStore implements AuditStore {

    public static final Table<Record> AUDIT = table("audit_event");

    public static final Field<Object> ID = field("id");
    public static final Field<Object> TYPE = field("type");
    public static final Field<Object> ACTION = field("action");
    public static final Field<Object> REFERENCE = field("reference");

    private DSLContext jooq;

    public PostgresAuditStore(DSLContext jooq) {
        this.jooq = jooq;
    }

    @Override
    public DomainResult<Long> saveEvent(AuditEventRecord event) {
        try {
            var insertSql = jooq.insertInto(AUDIT, TYPE, ACTION, REFERENCE)
                    .values(
                            event.getType().name(),
                            event.getAction().name(),
                            event.getReference()
                    );
            log.info(insertSql.getSQL());
            long data = insertSql.execute();
            return (data > 0) ? DomainResult.ok(data, "Saved AuditEvent") : DomainResult.invalidArgument("Failed to save AuditEvent " + event, null);
        } catch (Exception err) {
            return DomainResult.invalidArgument("Error saving AuditEvent " + event, err);
        }
    }

    @Override
    public DomainResult<List<AuditEventRecord>> loadEvents() {
        // fetch all Audit
        log.info("Loading AuditEvents");
        List<AuditEventRecord> results = new ArrayList<>();
        var fetchAllSql = jooq.select().from(AUDIT);
        log.info(fetchAllSql.getSQL());
        var fetchAll = fetchAllSql.fetch();
        fetchAll.forEach( record -> {
            var auditEventRecord = new AuditEventRecord(
                    record.get(ID).toString(),
                    AuditEventType.valueOf(record.get(TYPE).toString()),
                    AuditEventAction.valueOf(record.get(ACTION).toString()),
                    record.get(REFERENCE).toString(),
                    null
            );
            results.add(auditEventRecord);
        });
        log.info(DomainUtils.multiline("Fetched AuditEvents\n", results));
        return DomainResult.ok(results, "");
    }
}
