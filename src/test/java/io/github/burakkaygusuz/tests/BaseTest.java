package io.github.burakkaygusuz.tests;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.burakkaygusuz.utils.PropertyUtil;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Restful-Booker API Test Suite")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BaseTest {

    protected final PropertyUtil props = PropertyUtil.getInstance("app.properties");
    protected String token;

    @BeforeAll
    @Order(1)
    @DisplayName("Health Check")
    @Description("A simple health check endpoint to confirm whether the API is up and running")
    void healthCheck() {
        LogConfig logConfig = LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL);
        RestAssured.config = RestAssuredConfig.config().logConfig(logConfig);
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri(props.getProperty("uri"))
                .setContentType(ContentType.JSON)
                .setRelaxedHTTPSValidation()
                .build();

        Response response = given()
                .when()
                .get("/ping")
                .then()
                .extract().response();

        assertThat(response.statusCode())
                .as("Health check status")
                .isEqualTo(201);
    }

    @BeforeAll
    @Order(2)
    @DisplayName("Create a new token")
    @Description("Creates a new auth token to use for access to the PUT and DELETE /booking")
    void createToken() {
        String username = props.getProperty("username");
        String password = props.getProperty("password");
        final ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
        objectNode.put("username", username)
                .put("password", password);

        token = given()
                .body(objectNode)
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .extract().path("token");

        assertThat(token)
                .as("token")
                .isNotEmpty()
                .isInstanceOf(String.class);
    }

    @AfterAll
    void tearDown() {
        RestAssured.reset();
    }
}
