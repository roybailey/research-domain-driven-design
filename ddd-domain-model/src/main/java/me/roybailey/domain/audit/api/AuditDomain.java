package me.roybailey.domain.audit.api;

import me.roybailey.domain.DomainResult;
import me.roybailey.domain.audit.model.AuditEventRecord;

public interface AuditDomain {

    default DomainResult<Long> createEvent(AuditEventRecord event) {
        return DomainResult.notImplemented(this, "createEvent");
    }
}
