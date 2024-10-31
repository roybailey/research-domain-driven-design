package me.roybailey.domain.model.entitlements;

public record Entitlement(String name, String description) {
    public Entitlement(String name) {
        this(name, "Unknown");
    }
}
