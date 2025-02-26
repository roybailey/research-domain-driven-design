package me.roybailey.domain.entitlement.model;

import lombok.*;
import me.roybailey.domain.DomainEntity;

import java.util.Map;


@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Entitlement extends DomainEntity {

    String name;
    String description;

    @Builder
    public Entitlement(String id, String name, String description) {
        super(id);
        this.name = name;
        this.description = description;
    }

    public static Entitlement from(Map<String, Object> data) {
        return Entitlement.builder()
                .id((String) data.get("id"))
                .name((String)data.get("name"))
                .description((String)data.get("description"))
                .build();
    }

    public Map<String, Object> toMap(Map<String, Object> map) {
        map.put("id", getId());
        map.put("name", getName());
        map.put("description", getDescription());
        return map;
    }
}
