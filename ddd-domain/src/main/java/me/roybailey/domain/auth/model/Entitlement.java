package me.roybailey.domain.auth.model;

import lombok.Builder;
import lombok.Data;
import me.roybailey.domain.DomainEntity;

import java.util.Map;


@Data
@Builder
public class Entitlement extends DomainEntity {

    final String id;
    final String name;
    final String description;

    public static Entitlement from(Map<String, Object> data) {
        return Entitlement.builder()
                .id((String) data.get("id"))
                .name((String)data.get("name"))
                .description((String)data.get("description"))
                .build();
    }

    public Map<String, Object> toMap(Map<String, Object> map) {
        map.put("id", id);
        map.put("name", name);
        map.put("description", description);
        return map;
    }
}
