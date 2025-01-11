package me.roybailey.domain.entitlement;

import lombok.extern.slf4j.Slf4j;
import me.roybailey.domain.DomainAggregate;
import me.roybailey.domain.DomainResult;
import me.roybailey.domain.DomainUtils;
import me.roybailey.domain.audit.api.AuditDomain;
import me.roybailey.domain.audit.model.AuditEventRecord;
import me.roybailey.domain.entitlement.api.EntitlementDomain;
import me.roybailey.domain.entitlement.api.EntitlementStore;
import me.roybailey.domain.entitlement.model.Entitlement;
import me.roybailey.domain.entitlement.model.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Slf4j
public class EntitlementDomainService implements EntitlementDomain, DomainAggregate {

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
    public DomainResult<Map<String, Object>> getEntitlementsStats() {
        return entitlementsStore.getEntitlementStats();
    }

    @Override
    public DomainResult<Entitlement> createEntitlement(Entitlement entitlement) {
        log.info("Creating Entitlement {}", entitlement);
        entitlement.setId(UUID.randomUUID().toString());
        DomainResult<List<DomainResult<Entitlement>>> result = entitlementsStore.saveEntitlements(List.of(entitlement));
        if (result.isSuccess()) {
            DomainResult<Entitlement> createEntitlementResult = result.getData().get(0);
            if(createEntitlementResult.isSuccess()) {
                auditDomain.createEvent(AuditEventRecord.createEntitlement(entitlement.getName(), entitlement));
                log.info("Created Entitlement : {}", createEntitlementResult.getData());
                return DomainResult.ok(createEntitlementResult.getData(), result.getMessage());
            }
        } else {
            log.error("Failed to create Entitlement : "+ entitlement);
        }
        return DomainResult.result(result.getStatus(), null, result.getMessage(), null);
    }


    @Override
    public DomainResult<List<DomainResult<Entitlement>>> createEntitlements(List<Entitlement> entitlements) {
        log.info("Creating Entitlements "+entitlements.size());
        List<AuditEventRecord> events = new ArrayList<>(entitlements.size());
        for(Entitlement entitlement : entitlements) {
            entitlement.setId(UUID.randomUUID().toString());
        }
        var result = entitlementsStore.saveEntitlements(entitlements);
        if (result.isSuccess()) {
            for(int index = 0; index < entitlements.size(); index++) {
                Entitlement entitlement = entitlements.get(index);
                DomainResult<Entitlement> createEntitlementResult = result.getData().get(index);
                if(createEntitlementResult.isSuccess()) {
                    auditDomain.createEvent(AuditEventRecord.createEntitlement(entitlement.getName(), entitlement));
                }
            }
        }
        log.info(DomainUtils.multiline("Created Entitlements\n",entitlements));
        return result;
    }

    @Override
    public DomainResult<List<Entitlement>> getEntitlements(List<String> entitlementIds) {
        log.info("Fetching Entitlements "+entitlementIds.size());
        var result = entitlementsStore.findEntitlements(entitlementIds);
        log.info(DomainUtils.multiline("Found Entitlements\n",result.getData()));
        return result;
    }

    @Override
    public DomainResult<Long> deleteEntitlements(List<String> entitlementIds) {
        log.info("Deleting Entitlements {}", entitlementIds.size());
        var result = entitlementsStore.deleteEntitlements(entitlementIds);
        log.info("Deleted Entitlements {}", result.getData());
        return result;
    }


    @Override
    public DomainResult<List<DomainResult<Group>>> createGroups(List<Group> groups) {
        return DomainResult.notImplemented(this, "createGroups");
    }

    @Override
    public DomainResult<List<DomainResult<Package>>> createPackage(List<Package> packages) {
        return DomainResult.notImplemented(this, "createPackages");
    }
}
