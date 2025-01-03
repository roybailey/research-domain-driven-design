package me.roybailey.domain.entitlement.store;

import com.google.common.collect.Maps;
import me.roybailey.domain.DomainResult;
import me.roybailey.domain.DomainUtils;
import me.roybailey.domain.ResultStatus;
import me.roybailey.domain.entitlement.api.EntitlementStore;
import me.roybailey.domain.entitlement.model.Entitlement;
import me.roybailey.domain.entitlement.model.Group;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.fail;


public class Neo4jEntitlementStore implements EntitlementStore {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Driver neo4j;

    public Neo4jEntitlementStore(Driver neo4j) {
        this.neo4j = neo4j;
    }

    @Override
    public DomainResult<List<DomainResult<Long>>> saveEntitlements(List<Entitlement> entitlements) {
        logger.info("Saving entitlements {}, {}", entitlements.size(), neo4j.hashCode());
        List<DomainResult<Long>> saved = new ArrayList<>();
        long count = 0L;
        try (Session session = neo4j.session()) {
            try (Transaction tx = session.beginTransaction()) {
                for (Entitlement entitlement : entitlements) {
                    try {
                        var params = entitlement.toMap(Maps.newHashMap());
                        var rows = tx.run("""
                                    CREATE (e:Entitlement {
                                        id: $id,
                                        created: datetime(),
                                        name: $name,
                                        description: $description
                                    })
                                    RETURN count(e)
                                """, params).next().get(0).asLong();
                        logger.info("Nodes inserted {}", rows);
                        count += rows;
                        saved.add((rows > 0) ? DomainResult.ok(rows, "Saved Entitlement " + entitlement.getName()) :
                                DomainResult.invalidArgument("Failed to save entitlement " + entitlement.getName(), null));
                    } catch (Exception individualException) {
                        saved.add(DomainResult.invalidArgument("Error saving entitlement " + entitlement.getName(), individualException));
                    }
                }
                tx.commit();
            }
        } catch (Exception err) {
            return DomainResult.invalidArgument("Error saving entitlements", err);
        }
        return DomainResult.result(
                (count == entitlements.size()? ResultStatus.OK : ResultStatus.ERROR),
                saved,
                "Saved entitlements: "+count,
                null
        );
    }

    @Override
    public DomainResult<List<DomainResult<Long>>> saveGroups(List<Group> groups) {
        return EntitlementStore.super.saveGroups(groups);
    }

    @Override
    public DomainResult<List<DomainResult<Long>>> savePackage(List<Package> packages) {
        return EntitlementStore.super.savePackage(packages);
    }

    @Override
    public DomainResult<List<Package>> findPackages() {
        return EntitlementStore.super.findPackages();
    }

    @Override
    public DomainResult<List<Group>> findGroups() {
        return EntitlementStore.super.findGroups();
    }

    @Override
    public DomainResult<List<Entitlement>> findEntitlements() {
        logger.info("Finding entitlements {}", neo4j.hashCode());
        List<Entitlement> entitlements = new ArrayList<>();
        try (Session session = neo4j.session()) {
            var results = session.run("MATCH (e:Entitlement) return e", Collections.emptyMap());
            results.list().forEach(record -> {
                var entitlementsMap = record.get("e").asMap();
                entitlements.add(Entitlement.from(entitlementsMap));
            });
        } catch (Exception e) {
            fail(e.getMessage());
        }
        logger.info(DomainUtils.multiline("Fetched entitlements\n", entitlements));
        return DomainResult.ok(entitlements);
    }
}
