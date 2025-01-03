package me.roybailey.domain.audit.model;

import lombok.Builder;
import lombok.Data;
import me.roybailey.domain.DomainEntity;

import java.util.Map;


@Data
public class AuditEventRecord extends DomainEntity {

    final AuditEventType type;
    final AuditEventAction action;
    final String reference;
    final Map<String,Object> data;

    @Builder
    public AuditEventRecord(String id, AuditEventType type, AuditEventAction action, String reference, Map<String, Object> data) {
        super(id);
        this.type = type;
        this.action = action;
        this.reference = reference;
        this.data = data;
    }

    public static AuditEventRecord createEntitlement(String reference, Object entitlement) {
        return AuditEventRecord.builder()
                .type(AuditEventType.ENTITLEMENT)
                .action(AuditEventAction.CREATE)
                .reference(reference)
                .data(Map.of("entitlement", entitlement))
                .build();
    }
}

