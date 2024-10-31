package me.roybailey.domain.framework.audit;

import me.roybailey.domain.model.entitlements.Entitlement;

public record EntitlementEvent(
        AuditEventSubject subject,
        AuditEventType type,
        Entitlement entitlement
) implements AuditEvent {

    public static EntitlementEvent newCreateEntitlementEvent(Entitlement entitlement) {
        return new EntitlementEvent(AuditEventSubject.ENTITLEMENT, AuditEventType.CREATE, entitlement);
    }
}
