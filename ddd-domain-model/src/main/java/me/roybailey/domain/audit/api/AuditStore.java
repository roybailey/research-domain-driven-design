package me.roybailey.domain.audit.api;

import me.roybailey.domain.DomainResult;
import me.roybailey.domain.audit.model.AuditEventRecord;

import java.util.List;

public interface AuditStore {

    default DomainResult<Long> saveEvent(AuditEventRecord event) {
        return DomainResult.notImplemented(this, "saveEvent");
    }

    default DomainResult<List<AuditEventRecord>> loadEvents() {
        return DomainResult.notImplemented(this, "loadEvents");
    }
}
