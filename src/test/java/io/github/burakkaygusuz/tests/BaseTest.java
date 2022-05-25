package io.github.burakkaygusuz.tests;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseTest {

    protected final RequestSpecification requestSpecification;
    protected final SoftAssertions softly = new SoftAssertions();

    protected String token;

    public BaseTest() {
        LogConfig logConfig = LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL);
        RestAssuredConfig config = RestAssuredConfig.config().logConfig(logConfig);

        this.requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://restful-booker.herokuapp.com")
                .setContentType(ContentType.JSON)
                .setRelaxedHTTPSValidation()
                .setConfig(config)
                .build();
    }

    @BeforeAll
    @DisplayName("Health Check")
    void healthCheck() {
        Response response = given()
                .spec(requestSpecification)
                .when()
                .get("/ping")
                .then()
                .extract().response();

        assertThat(response.statusCode())
                .as("Healthcheck status")
                .isEqualTo(201);
    }

    @BeforeEach
    @DisplayName("Create a new token")
    void createToken() {
        final ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
        objectNode.put("username", "admin")
                .put("password", "password123");

        token = given()
                .spec(requestSpecification)
                .body(objectNode)
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .extract().path("token");

        assertThat(token).as("token").isNotEmpty();
    }

    @AfterEach
    void tearDown() {
        RestAssured.reset();
    }
}
