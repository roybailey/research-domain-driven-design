package me.roybailey.domain;

import me.roybailey.domain.entitlement.model.Entitlement;
import org.modelmapper.ModelMapper;
import roybailey.domain.openapi.model.EntitlementDto;

import java.util.List;

public class DomainMapper {

    static final ModelMapper modelMapper = new ModelMapper();

    public static Entitlement toEntitlement(EntitlementDto entitlementDto) {
        return modelMapper.map(entitlementDto, Entitlement.class);
    }

    public static List<Entitlement> toEntitlement(List<EntitlementDto> listEntitlementDtos) {
        return listEntitlementDtos.stream().map(DomainMapper::toEntitlement).toList();
    }

    public static EntitlementDto toEntitlementDto(Entitlement entitlement) {
        return modelMapper.map(entitlement, EntitlementDto.class);
    }

    public static List<EntitlementDto> toEntitlementDto(List<Entitlement> listEntitlements) {
        return listEntitlements.stream().map(DomainMapper::toEntitlementDto).toList();
    }

}
