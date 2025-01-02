package me.roybailey.domain.audit.store;

import me.roybailey.domain.audit.api.AuditDomain;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.springframework.stereotype.Service;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;


@Service
public class AuditPostgresStorage implements AuditDomain {

    public static final Table<Record> AUDIT = table("audit");
    public static final Field<Object> ID = field("id");
    public static final Field<Object> SUBJECT = field("subject");
    public static final Field<Object> TYPE = field("type");

}
