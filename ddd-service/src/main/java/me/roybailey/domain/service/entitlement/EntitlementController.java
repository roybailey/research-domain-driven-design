package me.roybailey.domain.service.entitlement;

import com.google.common.collect.Maps;
import me.roybailey.domain.entitlement.api.EntitlementDomain;
import me.roybailey.domain.entitlement.model.Entitlement;
import me.roybailey.domain.service.DomainApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
public class EntitlementController {

    @Autowired
    private EntitlementDomain entitlementDomain;

    @PostMapping
    public ResponseEntity<DomainApiResponse<Map<String,Object>>> upsertEntitlements(
            @RequestParam String callback,
            @RequestBody List<Entitlement> entitlements
    ) {
        var result = entitlementDomain.createEntitlements(entitlements);
        return (result.isSuccess()? ResponseEntity.ok(new DomainApiResponse<>(callback, entitlements.stream().map(entitlement -> {
            return entitlement.toMap(Maps.newHashMap());
        }).toList())) : ResponseEntity.badRequest().build());
    }
}
