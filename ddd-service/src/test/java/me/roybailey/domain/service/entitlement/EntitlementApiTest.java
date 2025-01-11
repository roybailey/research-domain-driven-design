package me.roybailey.domain.service.entitlement;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import roybailey.domain.openapi.model.EntitlementDto;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;


@SpringBootTest(classes = DomainServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class EntitlementApiTest extends DomainTestContainerBase {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
                .post("/entitlement-api/v1/entitlements")
                .then()
                .statusCode(200)
                .extract();

        var responseDto = Arrays.asList(response.as(EntitlementDto.class));
        logger.info(responseDto.getClass().getName());
        logger.info(responseDto.toString());
    }

    @Test
    @Order(20)
    @Disabled
    public void testGetEntitlements() {

        // Test the GET endpoint
        ExtractableResponse<Response> response = given()
                .when()
                .get("/entitlement-api/v1/entitlements")
                .then()
                .statusCode(200)
                .extract();

        var responseDto = response.as(Object.class);
        logger.info(responseDto.getClass().getName());
        logger.info(responseDto.toString());
    }

}
