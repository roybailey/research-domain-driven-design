package me.roybailey.domain.audit.model;

import me.roybailey.domain.audit.api.AuditEvent;
import me.roybailey.domain.auth.model.Entitlement;

public record EntitlementEvent(
        AuditEventSubject subject,
        AuditEventType type,
        Entitlement entitlement
) implements AuditEvent {

    public static EntitlementEvent newCreateEntitlementEvent(Entitlement entitlement) {
        return new EntitlementEvent(AuditEventSubject.ENTITLEMENT, AuditEventType.CREATE, entitlement);
    }
}
