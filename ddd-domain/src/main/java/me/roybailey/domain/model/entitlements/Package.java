package me.roybailey.domain.model.entitlements;

import java.util.Collections;
import java.util.List;

public record Package(String name, String description, List<Group> groups) {
    public Package(String name) {
        this(name, "Unknown", Collections.emptyList());
    }
}
