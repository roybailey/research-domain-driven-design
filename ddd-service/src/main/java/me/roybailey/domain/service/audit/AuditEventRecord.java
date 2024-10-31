package me.roybailey.domain.service.audit;

import me.roybailey.domain.framework.audit.AuditEvent;
import me.roybailey.domain.framework.audit.AuditEventSubject;
import me.roybailey.domain.framework.audit.AuditEventType;
import me.roybailey.domain.framework.audit.AuditUseCase;
import org.jetbrains.annotations.NotNull;
import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.Table;
import org.jooq.TableField;
import org.springframework.stereotype.Service;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;


public record AuditEventRecord(String id, AuditEventSubject subject, AuditEventType type) implements AuditEvent {
}
