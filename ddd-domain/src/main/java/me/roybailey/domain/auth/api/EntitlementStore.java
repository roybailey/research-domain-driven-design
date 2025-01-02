package me.roybailey.domain.auth.api;

import me.roybailey.domain.DomainResult;
import me.roybailey.domain.auth.model.Entitlement;
import me.roybailey.domain.auth.model.Group;

import java.util.List;

public interface EntitlementStore {

    default DomainResult<List<Entitlement>> saveEntitlements(List<Entitlement> entitlements) {
        return DomainResult.notImplemented(this, "saveEntitlements");
    }

    default DomainResult<List<Group>> saveGroups(List<Group> groups) {
        return DomainResult.notImplemented(this, "saveGroups");
    }

    default DomainResult<List<Package>> savePackage(List<Package> packages) {
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
