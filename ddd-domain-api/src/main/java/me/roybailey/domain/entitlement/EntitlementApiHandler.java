package me.roybailey.domain.entitlement;

import lombok.extern.slf4j.Slf4j;
import me.roybailey.domain.DomainMapper;
import me.roybailey.domain.DomainResult;
import me.roybailey.domain.ResultStatus;
import me.roybailey.domain.entitlement.api.EntitlementDomain;
import me.roybailey.domain.entitlement.model.Entitlement;
import me.roybailey.domain.openapi.api.AccessControlApiDelegate;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import me.roybailey.domain.openapi.model.DomainResultDto;
import me.roybailey.domain.openapi.model.EntitlementDto;

import java.util.*;

import static java.util.Collections.emptyList;


@Slf4j
public class EntitlementApiHandler implements AccessControlApiDelegate {

    private final NativeWebRequest webRequest;
    private final EntitlementDomain entitlementDomain;
    private final ModelMapper mapper = new ModelMapper();

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
    public ResponseEntity<DomainResultDto> aboutEntitlementService() {
        return AccessControlApiDelegate.super.aboutEntitlementService();
    }

    @Override
    public ResponseEntity<List<EntitlementDto>> createEntitlements(List<EntitlementDto> entitlementDtos) {
        var result = entitlementDomain.createEntitlements(DomainMapper.toEntitlement(entitlementDtos));
        var savedEntitlements = DomainMapper.toEntitlementDto(result.getOrElse(emptyList()).stream().map(DomainResult::getData).toList());
        return (result.isSuccess() ? ResponseEntity.ok(savedEntitlements) : ResponseEntity.badRequest().build());
    }

    @Override
    public ResponseEntity<Void> deleteEntitlements(List<String> ids) {
        var result = entitlementDomain.deleteEntitlements(ids);
        return (result.isSuccess() ? ResponseEntity.ok(null) : ResponseEntity.badRequest().build());
    }

    @Override
    public ResponseEntity<Void> deleteEntitlement(String entitlement) {
        return AccessControlApiDelegate.super.deleteEntitlement(entitlement);
    }

    @Override
    public ResponseEntity<EntitlementDto> getEntitlement(String entitlement) {
        return AccessControlApiDelegate.super.getEntitlement(entitlement);
    }

    @Override
    public ResponseEntity<List<EntitlementDto>> getEntitlements(List<String> ids, String group) {
        var result = (!ids.isEmpty())? entitlementDomain.getEntitlements(ids)
                : DomainResult.result(ResultStatus.NOT_IMPLEMENTED, (List<Entitlement>)null, "entitlementDomain.getEntitlementByGroup()", null);
        var entitlementDtos = DomainMapper.toEntitlementDto(result.getOrElse(emptyList()));
        return (result.isSuccess() ? ResponseEntity.ok(entitlementDtos) : ResponseEntity.badRequest().build());
    }

    @Override
    public ResponseEntity<Void> updateEntitlement(String entitlement, EntitlementDto body) {
        return AccessControlApiDelegate.super.updateEntitlement(entitlement, body);
    }
}
