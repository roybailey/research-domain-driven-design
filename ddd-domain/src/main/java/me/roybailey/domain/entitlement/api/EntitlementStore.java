package me.roybailey.domain.entitlement.api;

import me.roybailey.domain.DomainResult;
import me.roybailey.domain.entitlement.model.Entitlement;
import me.roybailey.domain.entitlement.model.Group;

import java.util.List;

public interface EntitlementStore {

    default DomainResult<List<DomainResult<Long>>> saveEntitlements(List<Entitlement> entitlements) {
        return DomainResult.notImplemented(this, "saveEntitlements");
    }

    default DomainResult<List<DomainResult<Long>>> saveGroups(List<Group> groups) {
        return DomainResult.notImplemented(this, "saveGroups");
    }

    default DomainResult<List<DomainResult<Long>>> savePackage(List<Package> packages) {
        return DomainResult.notImplemented(this, "savePackage");
    }

    default DomainResult<List<Package>> findPackages() {
        return DomainResult.notImplemented(this, "findPackages");
    }

    default DomainResult<List<Group>> findGroups() {
        return DomainResult.notImplemented(this, "findGroups");
    }

    default DomainResult<List<Entitlement>> findEntitlements() {
        return DomainResult.notImplemented(this, "findEntitlements");
    }

}
