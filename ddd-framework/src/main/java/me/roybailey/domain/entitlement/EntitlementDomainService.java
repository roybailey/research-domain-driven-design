package me.roybailey.domain.entitlement;

import me.roybailey.domain.DomainAggregate;
import me.roybailey.domain.DomainResult;
import me.roybailey.domain.DomainUtils;
import me.roybailey.domain.audit.api.AuditDomain;
import me.roybailey.domain.audit.model.AuditEventRecord;
import me.roybailey.domain.entitlement.api.EntitlementDomain;
import me.roybailey.domain.entitlement.api.EntitlementStore;
import me.roybailey.domain.entitlement.model.Entitlement;
import me.roybailey.domain.entitlement.model.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EntitlementDomainService implements EntitlementDomain, DomainAggregate {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    AuditDomain auditDomain;
    EntitlementStore entitlementsStore;

    public EntitlementDomainService(
            AuditDomain auditDomain,
            EntitlementStore entitlementsStore
    ) {
        this.auditDomain = auditDomain;
        this.entitlementsStore = entitlementsStore;
    }

    @Override
    public DomainResult<List<DomainResult<Long>>> createEntitlements(List<Entitlement> entitlements) {
        logger.info("Creating Entitlements "+entitlements.size());
        List<AuditEventRecord> events = new ArrayList<>(entitlements.size());
        for(Entitlement entitlement : entitlements) {
            entitlement.setId(UUID.randomUUID().toString());
        }
        var result = entitlementsStore.saveEntitlements(entitlements);
        if (result.isSuccess()) {
            for(int index = 0; index < entitlements.size(); index++) {
                Entitlement entitlement = entitlements.get(index);
                DomainResult<Long> createEntitlementResult = result.getData().get(index);
                if(createEntitlementResult.isSuccess()) {
                    auditDomain.createEvent(AuditEventRecord.createEntitlement(entitlement.getName(), entitlement));
                }
            }
        }
        logger.info(DomainUtils.multiline("Created Entitlements\n",entitlements));
        return result;
    }

    @Override
    public DomainResult<List<Group>> createGroups(List<Group> groups) {
        return EntitlementDomain.super.createGroups(groups);
    }

    @Override
    public DomainResult<List<Package>> createPackage(List<Package> packages) {
        return EntitlementDomain.super.createPackage(packages);
    }
}
