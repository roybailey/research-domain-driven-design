package me.roybailey.domain.service.entitlement;

import lombok.extern.slf4j.Slf4j;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import me.roybailey.domain.DomainServiceApplication;
import me.roybailey.domain.DomainTestContainerBase;
import me.roybailey.domain.audit.api.AuditDomain;
import me.roybailey.domain.audit.api.AuditStore;
import me.roybailey.domain.entitlement.api.EntitlementDomain;
import me.roybailey.domain.entitlement.api.EntitlementStore;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import me.roybailey.domain.openapi.model.EntitlementDto;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@SpringBootTest(classes = DomainServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class EntitlementApiTest extends DomainTestContainerBase {

    public static final String ACCESS_CONTROL_BASE = "/access-control/v1";

    @Autowired
    private AuditStore auditStore;
    @Autowired
    private AuditDomain auditDomain;
    @Autowired
    private EntitlementStore entitlementStore;
    @Autowired
    private EntitlementDomain entitlementDomain;

    private List<EntitlementDto> listEntitlements = List.of(
            EntitlementDto.builder().name("A").description("AAA").build(),
            EntitlementDto.builder().name("B").description("BBB").build(),
            EntitlementDto.builder().name("C").description("CCC").build()
    );
    private Map<String, String> mapEntitlements = new HashMap<>();

    @LocalServerPort
    private int port;

    @BeforeAll
    public static void configureRestAssured() {
        RestAssured.defaultParser = Parser.JSON;
    }

    @BeforeEach
    public void setup() {
        // Configure RestAssured to use the random port
        RestAssured.port = port;
    }

    @Test
    @Order(10)
    public void testCreateEntitlements() {

        var response = given().log().all()
                .body(listEntitlements)
                .header("Content-Type", MediaType.APPLICATION_JSON.toString())
                .when()
                .post(ACCESS_CONTROL_BASE+"/entitlements")
                .then()
                .statusCode(200)
                .extract();

        var responseDto = Arrays.asList(response.as(EntitlementDto[].class));
        log.info(responseDto.toString());
        for (EntitlementDto entitlementDto : responseDto) {
            assertThat(entitlementDto.getId()).isNotNull();
            mapEntitlements.put(entitlementDto.getId(), entitlementDto.getName());
            entitlementDto.id(null);
            assertThat(listEntitlements.contains(entitlementDto)).isTrue();
        }
    }

    @Test
    @Order(20)
    public void testGetEntitlements() {

        // Test the GET endpoint
        ExtractableResponse<Response> response = given()
                .when()
                .get(ACCESS_CONTROL_BASE+"/entitlements?ids="+mapEntitlements.keySet().stream().map(Object::toString).collect(Collectors.joining(",")))
                .then()
                .statusCode(200)
                .extract();

        var responseDto = Arrays.asList(response.as(EntitlementDto[].class));
        log.info(responseDto.toString());
        for (EntitlementDto entitlementDto : responseDto) {
            assertThat(entitlementDto.getId()).isNotNull();
            entitlementDto.id(null);
            assertThat(listEntitlements.contains(entitlementDto)).isTrue();
        }
    }


    @Test
    @Order(30)
    @Disabled
    public void testUpdateEntitlements() {

        listEntitlements.forEach(entilement -> {
            entilement.setDescription(entilement.getDescription() + "-UPDATED");
        });
        // Test the GET endpoint
        ExtractableResponse<Response> response = given()
                .body(listEntitlements)
                .header("Content-Type", MediaType.APPLICATION_JSON.toString())
                .when()
                .put(ACCESS_CONTROL_BASE+"/entitlements")
                .then()
                .statusCode(200)
                .extract();

        var responseDto = Arrays.asList(response.as(EntitlementDto[].class));
        log.info(responseDto.toString());
        for (EntitlementDto entitlementDto : responseDto) {
            assertThat(listEntitlements.contains(entitlementDto)).isTrue();
        }
    }


    @Test
    @Order(40)
    public void testDeleteEntitlements() {

        // Test the GET endpoint
        given()
                .when()
                .delete(ACCESS_CONTROL_BASE+"/entitlements?ids="+mapEntitlements.keySet().stream().map(Object::toString).collect(Collectors.joining(",")))
                .then()
                .statusCode(200);

    }
}
