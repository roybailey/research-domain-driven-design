package me.roybailey.domain.audit.api;

public interface AuditDomain {

    default String createEvent(AuditEvent event) {
        throw new RuntimeException("NOT IMPLEMENTED: "+this.getClass().getSimpleName()+".createEvent");
    }
}
