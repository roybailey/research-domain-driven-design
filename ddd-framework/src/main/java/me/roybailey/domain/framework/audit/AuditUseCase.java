package me.roybailey.domain.framework.audit;

public interface AuditUseCase {

    default String createEvent(AuditEvent event) {
        throw new RuntimeException("NOT IMPLEMENTED: "+this.getClass().getSimpleName()+".createEvent");
    }
}
