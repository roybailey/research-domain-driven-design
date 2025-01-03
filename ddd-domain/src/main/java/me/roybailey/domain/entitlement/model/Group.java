package me.roybailey.domain.entitlement.model;

import lombok.Builder;
import lombok.Data;
import me.roybailey.domain.DomainEntity;
import java.util.List;


@Data
public class Group extends DomainEntity {

    final String name;
    final String description;
    final List<Entitlement> entitlements;

    @Builder
    public Group(String id, String name, String description, List<Entitlement> entitlements) {
        super(id);
        this.name = name;
        this.description = description;
        this.entitlements = entitlements;
    }
}
