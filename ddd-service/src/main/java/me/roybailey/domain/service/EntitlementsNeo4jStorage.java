package me.roybailey.domain.service;

import me.roybailey.domain.framework.entitlements.EntitlementUseCase;
import me.roybailey.domain.model.entitlements.Entitlement;
import me.roybailey.domain.model.entitlements.Group;

import java.util.List;


public class EntitlementsNeo4jStorage implements EntitlementUseCase {

    @Override
    public List<Entitlement> createEntitlements(List<Entitlement> entitlements) {
        return List.of();
    }

    @Override
    public List<Group> createGroups(List<Group> groups) {
        return List.of();
    }

    @Override
    public List<Package> createPackage(List<Package> packages) {
        return List.of();
    }
}
