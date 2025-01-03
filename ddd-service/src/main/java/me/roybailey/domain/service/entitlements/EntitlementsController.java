package me.roybailey.domain.service.entitlements;

import me.roybailey.domain.audit.api.AuditDomain;
import me.roybailey.domain.audit.model.AuditEventRecord;
import me.roybailey.domain.auth.model.Entitlement;
import me.roybailey.domain.service.DomainApiResponse;
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
    private AuditDomain auditStore;

    @PostMapping
    public ResponseEntity<DomainApiResponse<Map<String,Object>>> upsertEntitlements(
            @RequestParam String callback,
            @RequestBody List<Entitlement> entitlements
    ) {
        List<Map<String,Object>> events = new ArrayList<>(entitlements.size());
        entitlements.forEach( entitlement -> {
            var eventId = auditStore.createEvent(AuditEventRecord.createEntitlement(entitlement.getName(), entitlement));
            events.add(Map.of("eventId", eventId, "entitlement", entitlement.getName()));
        });
        return ResponseEntity.ok(new DomainApiResponse<>(callback, events));
    }
}
