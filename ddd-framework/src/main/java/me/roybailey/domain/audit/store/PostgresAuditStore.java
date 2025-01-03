package me.roybailey.domain.audit.store;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;


@Service
public class PostgresAuditStore implements AuditStore {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final Table<Record> AUDIT = table("audit");

    public static final Field<Object> ID = field("id");
    public static final Field<Object> TYPE = field("type");
    public static final Field<Object> ACTION = field("action");
    public static final Field<Object> REFERENCE = field("reference");

    private DSLContext jooq;

    public PostgresAuditStore(DSLContext jooq) {
        this.jooq = jooq;
    }

    @Override
    public DomainResult<Integer> saveEvent(AuditEventRecord event) {
        try {
            var insertSql = jooq.insertInto(AUDIT, ID, TYPE, ACTION, REFERENCE)
                    .values(
                            event.getId(),
                            event.getType().name(),
                            event.getAction().name(),
                            event.getReference()
                    );
            logger.info(insertSql.getSQL());
            int data = insertSql.execute();
            return (data > 0) ? DomainResult.ok(data, "Saved AuditEvent") : DomainResult.invalidArgument("Failed to save AuditEvent " + event, null);
        } catch (Exception err) {
            return DomainResult.invalidArgument("Error saving AuditEvent " + event, err);
        }
    }

    @Override
    public DomainResult<List<AuditEventRecord>> loadEvents() {
        // fetch all Audit
        logger.info("Loading AuditEvents");
        List<AuditEventRecord> results = new ArrayList<>();
        var fetchAllSql = jooq.select().from(AUDIT);
        logger.info(fetchAllSql.getSQL());
        var fetchAll = fetchAllSql.fetch();
        fetchAll.forEach( record -> {
            var auditEventRecord = new AuditEventRecord(
                    record.get(ID).toString(),
                    AuditEventType.valueOf(record.get(TYPE).toString()),
                    AuditEventAction.valueOf(record.get(ACTION).toString()),
                    record.get(REFERENCE).toString(),
                    Collections.emptyMap()
            );
            results.add(auditEventRecord);
        });
        logger.info(DomainUtils.multiline("Fetched AuditEvents\n", results));
        return DomainResult.ok(results, "");
    }
}
