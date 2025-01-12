package me.roybailey.domain;

import lombok.extern.slf4j.Slf4j;
import me.roybailey.domain.audit.model.AuditEventAction;
import me.roybailey.domain.audit.model.AuditEventRecord;
import me.roybailey.domain.audit.model.AuditEventType;
import org.assertj.core.api.Assertions;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.SQLDataType;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.Map;

import static me.roybailey.domain.audit.PostgresAuditStore.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.jooq.impl.DSL.table;
import static org.junit.jupiter.api.Assertions.fail;


@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class DomainTestContainerTest extends DomainTestContainerBase {

    @Autowired
    public DSLContext jooq;

    @Autowired
    public Driver neo4j;

    @Test
    public void testPostgresTestContainerInitialized() {

        // re-use the Jooq Audit table column definitions but on a test table
        // to see if vanilla Jooq SQL is working on the connected database
        Table< Record > TEST_AUDIT = table("test_table");
        var dropSql = jooq.dropTableIfExists(TEST_AUDIT);
        log.info(dropSql.getSQL());
        dropSql.execute();

        var createSql = jooq.createTable(TEST_AUDIT)
                .column(ID.getName(), SQLDataType.VARCHAR(36))
                .column(TYPE.getName(), SQLDataType.VARCHAR)
                .column(ACTION.getName(), SQLDataType.VARCHAR)
                .column(REFERENCE.getName(), SQLDataType.VARCHAR)
                ;
        log.info(createSql.getSQL());
        createSql.execute();

        // save a few Audit
        for(int index = 0; index < 5; ++index) {
            var auditEventRecord = new AuditEventRecord(
                    java.util.UUID.randomUUID().toString(),
                    AuditEventType.values()[index % 3],
                    AuditEventAction.CREATE,
                    ""+index,
                    Collections.emptyMap()
            );
            log.info("Saving audit event "+auditEventRecord);
            var insertSql = jooq.insertInto(TEST_AUDIT, ID, TYPE, ACTION, REFERENCE)
                    .values(
                            auditEventRecord.getId(),
                            auditEventRecord.getType().name(),
                            auditEventRecord.getAction().name(),
                            auditEventRecord.getReference()
                    );
            log.info(insertSql.getSQL());
            insertSql.execute();
        }

        // fetch all Audit
        log.info("Audit found with findAll():");
        log.info("-------------------------------");
        var fetchAllSql = jooq.select().from(TEST_AUDIT);
        log.info(fetchAllSql.getSQL());
        var fetchAll = fetchAllSql.fetch();
        var auditData = fetchAll.map( record -> {
            var auditEventRecord = new AuditEventRecord(
                    record.get(ID).toString(),
                    AuditEventType.valueOf(record.get(TYPE).toString()),
                    AuditEventAction.valueOf(record.get(ACTION).toString()),
                    record.get(REFERENCE).toString(),
                    Collections.emptyMap()
            );
            log.info("Found Audit "+auditEventRecord);
            return auditEventRecord;
        }).stream().toList();
        log.info("");

        Assertions.assertThat(auditData.size()).isGreaterThanOrEqualTo(5);
        jooq.dropTableIfExists(TEST_AUDIT).execute();
    }


    @Test
    public void testNeo4jTestContainerInitialized() {

        try (Session session = neo4j.session()) {
            Map<String,?> mapNode = session.run("CREATE (n:Test { name: 'First' }) return n", Collections.emptyMap()).next().get(0).asMap();
            log.info("Created Test Node "+mapNode);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }

        try (Session session = neo4j.session()) {
            Long count = session.run("MATCH (n) return count(n)", Collections.emptyMap()).next().get(0).asLong();
            log.info("Node count {}", count);
            assertThat(count).isGreaterThanOrEqualTo(1);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertThat(neo4jDatabase).isNotNull();
    }
}
