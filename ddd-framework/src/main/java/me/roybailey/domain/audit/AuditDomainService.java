package me.roybailey.domain.audit;

import lombok.extern.slf4j.Slf4j;
import me.roybailey.domain.DomainAggregate;
import me.roybailey.domain.DomainResult;
import me.roybailey.domain.audit.api.AuditDomain;
import me.roybailey.domain.audit.api.AuditStore;
import me.roybailey.domain.audit.model.AuditEventRecord;


@Slf4j
public class AuditDomainService implements AuditDomain, DomainAggregate {

    AuditStore auditStore;

    public AuditDomainService(AuditStore auditStore) {
        this.auditStore = auditStore;
    }

    @Override
    public DomainResult<Long> createEvent(AuditEventRecord event) {
        return auditStore.saveEvent(event);
    }
}
