package me.roybailey.domain.audit;

import me.roybailey.domain.DomainAggregate;
import me.roybailey.domain.DomainResult;
import me.roybailey.domain.audit.api.AuditDomain;
import me.roybailey.domain.audit.api.AuditStore;
import me.roybailey.domain.audit.model.AuditEventRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class AuditDomainService implements AuditDomain, DomainAggregate {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    AuditStore auditStore;

    public AuditDomainService(AuditStore auditStore) {
        this.auditStore = auditStore;
    }

    @Override
    public DomainResult<Long> createEvent(AuditEventRecord event) {
        return auditStore.saveEvent(event);
    }
}
