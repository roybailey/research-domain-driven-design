package me.roybailey.domain.entitlement.api;

import me.roybailey.domain.DomainResult;
import me.roybailey.domain.entitlement.model.Entitlement;
import me.roybailey.domain.entitlement.model.Group;

import java.util.List;
import java.util.Map;


public interface EntitlementDomain {

    DomainResult<Map<String,Object>> getEntitlementsStats();

    DomainResult<Entitlement> createEntitlement(Entitlement entitlement);
    DomainResult<List<DomainResult<Entitlement>>> createEntitlements(List<Entitlement> entitlements);
    DomainResult<List<Entitlement>> getEntitlements(List<String> entitlementIds);
    DomainResult<Long> deleteEntitlements(List<String> entitlementIds);

    DomainResult<List<DomainResult<Group>>> createGroups(List<Group> groups);

    DomainResult<List<DomainResult<Package>>> createPackage(List<Package> packages);

}
