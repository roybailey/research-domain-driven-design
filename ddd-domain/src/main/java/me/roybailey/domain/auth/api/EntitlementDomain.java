package me.roybailey.domain.auth.api;

import me.roybailey.domain.DomainResult;
import me.roybailey.domain.auth.model.Entitlement;
import me.roybailey.domain.auth.model.Group;

import java.util.List;

public interface EntitlementDomain {

    default DomainResult<List<DomainResult<Long>>> createEntitlements(List<Entitlement> entitlements) {
        return DomainResult.notImplemented(this, "createEntitlements");
    }

    default DomainResult<List<Group>> createGroups(List<Group> groups) {
        return DomainResult.notImplemented(this, "createGroups");
    }

    default DomainResult<List<Package>> createPackage(List<Package> packages) {
        return DomainResult.notImplemented(this, "createPackage");
    }

}
