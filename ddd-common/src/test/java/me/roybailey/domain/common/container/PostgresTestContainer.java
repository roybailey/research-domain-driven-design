package me.roybailey.domain.common.container;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitAllStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class PostgresTestContainer {

    @Container
    static PostgreSQLContainer container;

    public static PostgreSQLContainer create(
            String initScript,
            Boolean reuse,
            Boolean start
    ) {
        container = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.2").asCompatibleSubstituteFor("postgres"));
        container.withDatabaseName("testdb");
        container.withUsername("test");
        container.withPassword("password");
        container.withReuse(reuse);
        // add initialization script
        if (!(initScript == null || initScript.isEmpty())) {
            container.withInitScript(initScript);
        }
        // configure wait strategy for container to be ready
        container.waitingFor(
            new WaitAllStrategy()
                .withStrategy(Wait.forListeningPort())
                .withStrategy(
                    Wait.forLogMessage(".*database system is ready to accept connections.*\\s", 2)
                        .withStartupTimeout(Duration.ofSeconds(60))
                )
        );
        // start container
        if(start) {
            container.start();
        }
        return container;
    }

    /**
     * Call this method from within your
     *
     *  @JvmStatic
     *  @DynamicPropertySource
     *  fun properties(registry: DynamicPropertyRegistry) {
     *      registry.add("spring.datasource.url", container::getJdbcUrl);
     *      registry.add("spring.datasource.password", container::getPassword);
     *      registry.add("spring.datasource.username", container::getUsername);
     *  }
     */
    public static void registerSpringProperties(
            DynamicPropertyRegistry registry,
            String... prefixes
    ) {
        List<String> prefixList = Arrays.asList(prefixes);
        if(prefixList.isEmpty()) {
            prefixList.add("spring.datasource");
        }
        prefixList.forEach(prefix -> {
            registry.add("$prefix.url", container::getJdbcUrl);
            registry.add("$prefix.password", container::getPassword);
            registry.add("$prefix.username", container::getUsername);
        });
    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registerSpringProperties(registry, "spring.datasource");
    }

}
