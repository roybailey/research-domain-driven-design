package me.roybailey.domain.service;

import me.roybailey.domain.framework.audit.AuditEventSubject;
import me.roybailey.domain.framework.audit.AuditEventType;
import me.roybailey.domain.service.audit.AuditEventRecord;
import org.jooq.DSLContext;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Collections;
import java.util.Map;

import static me.roybailey.domain.service.audit.AuditPostgresStorage.*;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.junit.Assert.fail;

// under test source because it's only needed for testing
@SpringBootApplication
class DomainTestSpringBootApplication {

    private static Logger logger = LoggerFactory.getLogger(DomainTestSpringBootApplication.class);

    @Bean
    public CommandLineRunner demoPostgres(DSLContext jooq) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                var dropSql = jooq.dropTableIfExists(AUDIT);
                logger.info(dropSql.getSQL());
                dropSql.execute();

                var createSql = jooq.createTable(AUDIT)
                        .column(ID.getName(), VARCHAR(36))
                        .column(SUBJECT.getName(), VARCHAR)
                        .column(TYPE.getName(), VARCHAR);
                logger.info(createSql.getSQL());
                createSql.execute();

                // save a few Audit
                for(int index = 0; index < 5; ++index) {
                    var auditEventRecord = new AuditEventRecord(
                            java.util.UUID.randomUUID().toString(),
                            AuditEventSubject.values()[index % 3],
                            AuditEventType.CREATE
                    );
                    logger.info("Saving audit event "+auditEventRecord);
                    var insertSql = jooq.insertInto(AUDIT, ID, SUBJECT, TYPE)
                            .values(
                                    auditEventRecord.id(),
                                    auditEventRecord.subject().name(),
                                    auditEventRecord.type().name()
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
                fetchAll.forEach( record -> {
                    var auditEventRecord = new AuditEventRecord(
                            record.get(ID).toString(),
                            AuditEventSubject.valueOf(record.get(SUBJECT).toString()),
                            AuditEventType.valueOf(record.get(TYPE).toString())
                    );
                    logger.info("Found Audit "+auditEventRecord);
                });
                logger.info("");
            }
        };
    }

    @Bean
    public CommandLineRunner demoNeo4j(Driver neo4j) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                try (Session session = neo4j.session()) {
                    Map<String,?> mapPackage = session.run("CREATE (p:Package { name: 'First' }) return p", Collections.emptyMap()).next().get(0).asMap();
                    logger.info("Created Package "+mapPackage);
                } catch (Exception e) {
                    fail(e.getMessage());
                }
            }
        };
    }
}
