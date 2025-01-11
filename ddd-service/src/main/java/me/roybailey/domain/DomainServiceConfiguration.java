package me.roybailey.domain;

import me.roybailey.domain.audit.AuditDomainService;
import me.roybailey.domain.audit.api.AuditDomain;
import me.roybailey.domain.audit.api.AuditStore;
import me.roybailey.domain.audit.store.PostgresAuditStore;
import me.roybailey.domain.entitlement.EntitlementDomainService;
import me.roybailey.domain.entitlement.api.EntitlementDomain;
import me.roybailey.domain.entitlement.api.EntitlementStore;
import me.roybailey.domain.entitlement.store.Neo4jEntitlementStore;
import me.roybailey.domain.service.entitlement.EntitlementApiHandler;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.NativeWebRequest;


@Configuration
public class DomainServiceConfiguration {

    @Autowired
    NativeWebRequest webRequest;

    @Autowired
    DSLContext jooq;

    @Autowired
    protected org.neo4j.driver.Driver neo4j;

    @Bean
    AuditStore auditStore() {
        return new PostgresAuditStore(jooq);
    }

    @Bean
    AuditDomain auditDomain() {
        return new AuditDomainService(auditStore());
    }

    @Bean
    EntitlementStore entitlementStore() {
        return new Neo4jEntitlementStore(neo4j);
    }

    @Bean
    EntitlementDomain entitlementDomain() {
        return new EntitlementDomainService(auditDomain(),entitlementStore());
    }

    @Bean
    EntitlementApiHandler entitlementApiHandler() {
        return new EntitlementApiHandler(webRequest, entitlementDomain());
    }

}
