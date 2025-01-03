package me.roybailey.domain.audit;

import me.roybailey.domain.DomainAggregate;
import me.roybailey.domain.DomainResult;
import me.roybailey.domain.audit.api.AuditDomain;
import me.roybailey.domain.audit.api.AuditStore;
import me.roybailey.domain.audit.model.AuditEventRecord;
import me.roybailey.domain.auth.api.EntitlementDomain;
import me.roybailey.domain.auth.api.EntitlementStore;
import me.roybailey.domain.auth.model.Entitlement;
import me.roybailey.domain.auth.model.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

public class AuditDomainService implements AuditDomain, DomainAggregate {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    AuditStore auditStore;

    public AuditDomainService(AuditStore auditStore) {
        this.auditStore = auditStore;
    }

    @Override
    public DomainResult<Integer> createEvent(AuditEventRecord event) {
        event.setId(UUID.randomUUID().toString());
        return auditStore.saveEvent(event);
    }
}
