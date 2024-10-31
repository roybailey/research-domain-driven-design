package me.roybailey.domain.service.audit;

import me.roybailey.domain.framework.audit.AuditUseCase;
import org.jooq.Field;
import org.jooq.Table;
import org.springframework.stereotype.Service;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;


@Service
public class AuditPostgresStorage implements AuditUseCase {

    public static final Table AUDIT = table("audit");
    public static final Field ID = field("id");
    public static final Field SUBJECT = field("subject");
    public static final Field TYPE = field("type");

}
