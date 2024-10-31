package me.roybailey.domain.service;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DataSourceConnectionProvider;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import javax.sql.DataSource;

import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.fail;

@Configuration
class DomainTestServiceConfiguration {

    // Spring Boot has already initialized the datasource based on properties
    @Autowired
    DataSource dataSource;

    @Bean
    public DataSourceConnectionProvider connectionProvider() {
        return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource));
    }

    // Spring Boot has already created a transactionManager for us to take advantage of transaction support
    @Bean
    public DSLContext dsl() {
        var jooq = DSL.using(connectionProvider(), SQLDialect.POSTGRES);
        return jooq;
    }

    @Value("${neo4j.url}")
    public String neo4jUrl;

    @Bean
    public Driver neo4jDriver() {
        // Retrieve the Bolt URL from the container
        Driver driver = GraphDatabase.driver(neo4jUrl, AuthTokens.none());
        try (Session session = driver.session()) {
            long one = session.run("RETURN 1", Collections.emptyMap()).next().get(0).asLong();
            assertThat(one).isEqualTo(1L);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        return driver;
    }
}
