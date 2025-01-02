package me.roybailey.domain.audit.api;

import me.roybailey.domain.audit.model.AuditEventSubject;
import me.roybailey.domain.audit.model.AuditEventType;

public interface AuditEvent {

    AuditEventSubject subject();
    AuditEventType type();

}
