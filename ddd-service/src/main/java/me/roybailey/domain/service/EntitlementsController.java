package me.roybailey.domain.service;

import me.roybailey.domain.framework.audit.AuditUseCase;
import me.roybailey.domain.framework.audit.EntitlementEvent;
import me.roybailey.domain.model.entitlements.Entitlement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
public class EntitlementsController {

    @Autowired
    private AuditUseCase auditStore;

    @PostMapping
    public ResponseEntity<DomainApiResponse<Map<String,String>>> upsertEntitlements(
            @RequestParam String callback,
            @RequestBody List<Entitlement> entitlements
    ) {
        List<Map<String,String>> events = new ArrayList<>(entitlements.size());
        entitlements.forEach( entitlement -> {
            var eventId = auditStore.createEvent(EntitlementEvent.newCreateEntitlementEvent(entitlement));
            events.add(Map.of("eventId", eventId, "entitlement", entitlement.name()));
        });
        return ResponseEntity.ok(new DomainApiResponse<>(callback, events));
    }
}
