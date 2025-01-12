package me.roybailey.domain.entitlement.api;

import me.roybailey.domain.DomainResult;
import me.roybailey.domain.entitlement.model.Entitlement;
import me.roybailey.domain.entitlement.model.Group;

import java.util.List;
import java.util.Map;


public interface EntitlementStore {

    DomainResult<Map<String,Object>> getEntitlementStats();

    DomainResult<List<DomainResult<Entitlement>>> saveEntitlements(List<Entitlement> entitlements);
    DomainResult<List<Entitlement>> findEntitlements(List<String> entitlementIds);
    DomainResult<Long> deleteEntitlements(List<String> entitlementIds);

    DomainResult<List<DomainResult<Long>>> saveGroups(List<Group> groups);
    DomainResult<List<DomainResult<Long>>> savePackage(List<Package> packages);

    DomainResult<List<Package>> findPackages();
    DomainResult<List<Group>> findGroups();

}
