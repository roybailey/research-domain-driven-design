package me.roybailey.domain.framework.audit;

public interface AuditEvent {

    AuditEventSubject subject();
    AuditEventType type();

}
