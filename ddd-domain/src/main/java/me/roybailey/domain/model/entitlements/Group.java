package me.roybailey.domain.model.entitlements;

import java.util.Collections;
import java.util.List;

public record Group(String name, String description, List<Entitlement> entitlements) {
    public Group(String name) {
        this(name, "Unknown", Collections.emptyList());
    }
}
