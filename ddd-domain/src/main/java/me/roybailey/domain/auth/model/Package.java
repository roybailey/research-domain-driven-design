package me.roybailey.domain.auth.model;

import lombok.Builder;
import lombok.Data;
import me.roybailey.domain.DomainEntity;

import java.util.ArrayList;
import java.util.List;


@Data
@Builder
public class Package extends DomainEntity {

    final String name;
    final String description;
    final List<Group> groups;

}
