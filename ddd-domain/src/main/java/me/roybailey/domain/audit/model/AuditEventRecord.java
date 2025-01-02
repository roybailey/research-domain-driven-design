package me.roybailey.domain.audit.model;

import lombok.Builder;
import lombok.Data;
import me.roybailey.domain.DomainEntity;


@Data
public class AuditEventRecord extends DomainEntity {

    final AuditEventSubject subject;
    final AuditEventType type;

    @Builder
    public AuditEventRecord(String id, AuditEventSubject subject, AuditEventType type) {
        super(id);
        this.subject = subject;
        this.type = type;
    }

}

