package me.roybailey.domain.entitlement;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import me.roybailey.domain.DomainResult;
import me.roybailey.domain.DomainUtils;
import me.roybailey.domain.ResultStatus;
import me.roybailey.domain.entitlement.api.EntitlementStore;
import me.roybailey.domain.entitlement.model.Entitlement;
import me.roybailey.domain.entitlement.model.Group;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;

import java.util.*;

import static org.junit.Assert.fail;

@Slf4j
public class Neo4jEntitlementStore implements EntitlementStore {

    private Driver neo4j;

    public Neo4jEntitlementStore(Driver neo4j) {
        this.neo4j = neo4j;
    }

    private Map<String, String> statQueries = Map.of("totalEntitlements","MATCH (e:Entitlement) return count(e) as value");

    @Override
    public DomainResult<Map<String, Object>> getEntitlementStats() {
        log.info("Fetching entitlements stats");
        Map<String,Object> stats = new HashMap<>();
        try (Session session = neo4j.session()) {
            statQueries.keySet().forEach( name -> {
                stats.put(name, session.run(statQueries.get(name), Collections.emptyMap()).single().get("value").asLong());
            });
        } catch (Exception e) {
            fail(e.getMessage());
        }
        log.info("Fetched entitlement stats {}", stats);
        return DomainResult.ok(stats);
    }

    @Override
    public DomainResult<List<DomainResult<Entitlement>>> saveEntitlements(List<Entitlement> entitlements) {
        log.info("Saving entitlements {}", entitlements.size());
        List<DomainResult<Entitlement>> saved = new ArrayList<>();
        try (Session session = neo4j.session()) {
            session.executeWrite( tx -> {
                for (Entitlement entitlement : entitlements) {
                    try {
                        var params = entitlement.toMap(Maps.newHashMap());
                        var result = tx.run("""
                                    CREATE (e:Entitlement {
                                        id: $id,
                                        created: datetime(),
                                        name: $name,
                                        description: $description
                                    })
                                    RETURN e
                                """, params);
                        var savedEntitlement = Entitlement.from(result.single().get("e").asMap());
                        var summary = result.consume();
                        log.info("Entitlement Node inserted {}", summary);
                        saved.add((summary.counters().nodesCreated() > 0) ? DomainResult.ok(savedEntitlement, "Saved Entitlement " + savedEntitlement) :
                                DomainResult.invalidArgument("Failed to save entitlement " + entitlement, null));
                    } catch (Exception individualException) {
                        saved.add(DomainResult.invalidArgument("Error saving entitlement " + entitlement, individualException));
                    }
                }
                return saved;
            });
        } catch (Exception err) {
            return DomainResult.invalidArgument("Error saving entitlements", err);
        }
        return DomainResult.result(
                (saved.size() == entitlements.size()? ResultStatus.OK : ResultStatus.ERROR),
                saved,
                "Saved entitlements: "+saved.size(),
                null
        );
    }

    @Override
    public DomainResult<List<Entitlement>> findEntitlements(List<String> entitlementIds) {
        log.info("Finding entitlements {}", entitlementIds);
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
        log.info(DomainUtils.multiline("Fetched entitlements\n", entitlements));
        return DomainResult.ok(entitlements);
    }

    @Override
    public DomainResult<Long> deleteEntitlements(List<String> entitlementIds) {
        log.info("Deleting entitlements {}", entitlementIds);
        long result = 0L;
        try (Session session = neo4j.session()) {
            var results = session.run("MATCH (e:Entitlement) WHERE e.id in $id DELETE e", Map.of("id", entitlementIds));
            result = results.consume().counters().nodesDeleted();
        } catch (Exception e) {
            fail(e.getMessage());
        }
        log.info("Deleted entitlements {}", result);
        return DomainResult.ok(result);
    }


    @Override
    public DomainResult<List<DomainResult<Long>>> saveGroups(List<Group> groups) {
        return DomainResult.notImplemented(this, "saveGroups");
    }

    @Override
    public DomainResult<List<Group>> findGroups() {
        return DomainResult.notImplemented(this, "findGroups");
    }


    @Override
    public DomainResult<List<DomainResult<Long>>> savePackage(List<Package> packages) {
        return DomainResult.notImplemented(this, "savePackage");
    }

    @Override
    public DomainResult<List<Package>> findPackages() {
        return DomainResult.notImplemented(this, "findPackages");
    }

}
