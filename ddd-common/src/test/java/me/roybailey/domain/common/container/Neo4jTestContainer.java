package me.roybailey.domain.common.container;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.containers.Neo4jLabsPlugin;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitAllStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class Neo4jTestContainer {

//    public static class Neo4jContainer extends Neo4jContainer<Neo4jContainer> {
//        Neo4jContainer() {
//            super("neo4j:5.20.0-community");
//        }
//    }

    @Container
    static Neo4jContainer<?> container;

    public static Neo4jContainer<?> create(
            String password,
            String importFolder,
            Boolean apoc,
            Boolean reuse,
            Boolean start
    ) {
        container = new Neo4jContainer<>(DockerImageName.parse("neo4j:5.20.0-community").asCompatibleSubstituteFor("neo4j"));
        container
            .withAdminPassword(password)
            .withNeo4jConfig("dbms.security.auth_enabled", "false")
            .withNeo4jConfig("apoc.import.file.enabled", "true")
            .withReuse(reuse);
        // add plugins
        if (apoc) {
            container.withLabsPlugins(Neo4jLabsPlugin.APOC);
        }
        // add import folder mapping
        if (!(importFolder == null || importFolder.isEmpty())) {
            container.withCopyFileToContainer(
                MountableFile.forClasspathResource("/import/"),
                importFolder
            );
        }
        // configure wait strategy for container to be ready
        container.waitingFor(
            new WaitAllStrategy()
                .withStartupTimeout(Duration.ofMinutes(5))
                //.withStrategy(Wait.forListeningPort())
                .withStrategy(
                    Wait.forLogMessage(".*Bolt enabled on.*", 1)
                        .withStartupTimeout(Duration.ofSeconds(360))
                )
        );
        // start container
        if (start) {
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
            prefixList.add("neo4j");
            prefixList.add("spring.neo4j");
        }
        prefixList.forEach(prefix -> {
            registry.add("$prefix.uri", container::getBoltUrl);
            registry.add("$prefix.authentication.username", "neo4j"::toString);
            registry.add("$prefix.authentication.password", container::getAdminPassword);
        });
    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registerSpringProperties(registry);
    }

}
