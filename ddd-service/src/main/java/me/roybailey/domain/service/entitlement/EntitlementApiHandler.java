package me.roybailey.domain.service.entitlement;

import me.roybailey.domain.DomainResult;
import me.roybailey.domain.entitlement.api.EntitlementDomain;
import me.roybailey.domain.entitlement.model.Entitlement;
import me.roybailey.domain.openapi.api.EntitlementApiApiDelegate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import roybailey.domain.openapi.model.EntitlementDto;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


public class EntitlementApiHandler implements EntitlementApiApiDelegate {

    private final NativeWebRequest webRequest;
    private final EntitlementDomain entitlementDomain;

    public EntitlementApiHandler(
            NativeWebRequest webRequest,
            EntitlementDomain entitlementDomain
    ) {
        this.webRequest = webRequest;
        this.entitlementDomain = entitlementDomain;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.of(webRequest);
    }

    @Override
    public ResponseEntity<Object> aboutEntitlementService() {
        return EntitlementApiApiDelegate.super.aboutEntitlementService();
    }

    @Override
    public ResponseEntity<EntitlementDto> createEntitlements(List<EntitlementDto> entitlementDtos) {
        var result = entitlementDomain.createEntitlements(
                entitlementDtos.stream().map(dto -> Entitlement.builder().name(dto.getName()).build()).toList()
        );
        return (result.isSuccess() ? ResponseEntity.ok(entitlementDtos.get(0)) : ResponseEntity.badRequest().build());
    }

    @Override
    public ResponseEntity<Void> deleteEntitlements(List<EntitlementDto> entitlementDto) {
        return EntitlementApiApiDelegate.super.deleteEntitlements(entitlementDto);
    }

    @Override
    public ResponseEntity<Void> deleteEntitlement(String entitlement) {
        return EntitlementApiApiDelegate.super.deleteEntitlement(entitlement);
    }

    @Override
    public ResponseEntity<EntitlementDto> getEntitlement(String entitlement) {
        return EntitlementApiApiDelegate.super.getEntitlement(entitlement);
    }

    @Override
    public ResponseEntity<List<EntitlementDto>> getEntitlements(String group) {
        var result = entitlementDomain.getEntitlements(Arrays.asList("1", "2", "3"));
        var entitlementDtos = result.getOrElse(Collections.emptyList()).stream()
                .filter(DomainResult::isSuccess)
                .map(DomainResult::getData)
                .map(entitlement ->
                        EntitlementDto.builder()
                                .name(entitlement.getName())
                                .description(entitlement.getDescription())
                                .name(entitlement.getName())
                                .build()
                ).toList();
        return (result.isSuccess() ? ResponseEntity.ok(entitlementDtos) : ResponseEntity.badRequest().build());
    }

    @Override
    public ResponseEntity<Void> updateEntitlement(String entitlement, EntitlementDto body) {
        return EntitlementApiApiDelegate.super.updateEntitlement(entitlement, body);
    }
}
