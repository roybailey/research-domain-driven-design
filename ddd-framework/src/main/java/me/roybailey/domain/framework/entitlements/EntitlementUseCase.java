package me.roybailey.domain.framework.entitlements;

import me.roybailey.domain.model.entitlements.Entitlement;
import me.roybailey.domain.model.entitlements.Group;

import java.util.List;

public interface EntitlementUseCase {

    default List<Entitlement> createEntitlements(List<Entitlement> entitlements) {
        throw new RuntimeException("NOT IMPLEMENTED: "+this.getClass().getSimpleName()+".createEntitlements");
    }

    default List<Group> createGroups(List<Group> groups) {
        throw new RuntimeException("NOT IMPLEMENTED: "+this.getClass().getSimpleName()+".createGroups");
    }

    default List<Package> createPackage(List<Package> packages) {
        throw new RuntimeException("NOT IMPLEMENTED: "+this.getClass().getSimpleName()+".createPackage");
    }

}
