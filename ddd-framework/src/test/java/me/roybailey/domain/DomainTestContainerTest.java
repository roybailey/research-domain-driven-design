package me.roybailey.domain;

import me.roybailey.domain.audit.model.AuditEventAction;
import me.roybailey.domain.audit.model.AuditEventRecord;
import me.roybailey.domain.audit.model.AuditEventType;
import me.roybailey.domain.container.Neo4jTestContainer;
import me.roybailey.domain.container.PostgresTestContainer;
import org.assertj.core.api.Assertions;
import org.jooq.DSLContext;
import org.jooq.impl.SQLDataType;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collections;
import java.util.Map;

import static me.roybailey.domain.audit.store.PostgresAuditStore.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;


@SpringBootTest
@ActiveProfiles("test")
public class DomainTestContainerTest extends DomainTestContainerBase {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public DSLContext jooq;

    @Autowired
    public Driver neo4j;

    @Test
    public void testPostgresTestContainerInitialized() {

        var dropSql = jooq.dropTableIfExists(AUDIT);
        logger.info(dropSql.getSQL());
        dropSql.execute();

        var createSql = jooq.createTable(AUDIT)
                .column(ID.getName(), SQLDataType.VARCHAR(36))
                .column(TYPE.getName(), SQLDataType.VARCHAR)
                .column(ACTION.getName(), SQLDataType.VARCHAR)
                .column(REFERENCE.getName(), SQLDataType.VARCHAR)
                ;
        logger.info(createSql.getSQL());
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
            logger.info("Saving audit event "+auditEventRecord);
            var insertSql = jooq.insertInto(AUDIT, ID, TYPE, ACTION, REFERENCE)
                    .values(
                            auditEventRecord.getId(),
                            auditEventRecord.getType().name(),
                            auditEventRecord.getAction().name(),
                            auditEventRecord.getReference()
                    );
            logger.info(insertSql.getSQL());
            insertSql.execute();
        }

        // fetch all Audit
        logger.info("Audit found with findAll():");
        logger.info("-------------------------------");
        var fetchAllSql = jooq.select().from(AUDIT);
        logger.info(fetchAllSql.getSQL());
        var fetchAll = fetchAllSql.fetch();
        var auditData = fetchAll.map( record -> {
            var auditEventRecord = new AuditEventRecord(
                    record.get(ID).toString(),
                    AuditEventType.valueOf(record.get(TYPE).toString()),
                    AuditEventAction.valueOf(record.get(ACTION).toString()),
                    record.get(REFERENCE).toString(),
                    Collections.emptyMap()
            );
            logger.info("Found Audit "+auditEventRecord);
            return auditEventRecord;
        }).stream().toList();
        logger.info("");

        Assertions.assertThat(auditData.size()).isGreaterThanOrEqualTo(5);
        jooq.dropTableIfExists(AUDIT).execute();
    }


    @Test
    public void testNeo4jTestContainerInitialized() {

        try (Session session = neo4j.session()) {
            Map<String,?> mapNode = session.run("CREATE (n:Test { name: 'First' }) return n", Collections.emptyMap()).next().get(0).asMap();
            logger.info("Created Test Node "+mapNode);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }

        try (Session session = neo4j.session()) {
            Long count = session.run("MATCH (n) return count(n)", Collections.emptyMap()).next().get(0).asLong();
            logger.info("Node count {}", count);
            assertThat(count).isGreaterThanOrEqualTo(1);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertThat(neo4jDatabase).isNotNull();
    }
}
