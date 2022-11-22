package io.github.burakkaygusuz.tests;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.burakkaygusuz.utils.PropertyUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;

import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Restful-Booker API Test Suite")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseTest {

    protected final Properties props = PropertyUtils.getInstance().loadProperties("app.properties");
    protected final RequestSpecification requestSpecification;
    protected String token;

    public BaseTest() {
        LogConfig logConfig = LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL);
        RestAssuredConfig config = RestAssuredConfig.config().logConfig(logConfig);
        String uri = props.getProperty("uri");

        this.requestSpecification = new RequestSpecBuilder()
                .setBaseUri(uri)
                .setContentType(ContentType.JSON)
                .setRelaxedHTTPSValidation()
                .setConfig(config)
                .build();
    }

    @BeforeAll
    @DisplayName("Health Check")
    @Description("A simple health check endpoint to confirm whether the API is up and running")
    void healthCheck() {
        Response response = given()
                .spec(requestSpecification)
                .when()
                .get("/ping")
                .then()
                .extract().response();

        assertThat(response.statusCode())
                .as("Health check status")
                .isEqualTo(201);
    }

    @BeforeEach
    @DisplayName("Create a new token")
    @Description("Creates a new auth token to use for access to the PUT and DELETE /booking")
    void createToken() {
        String username = props.getProperty("username");
        String password = props.getProperty("password");
        final ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
        objectNode.put("username", username)
                .put("password", password);

        token = given()
                .spec(requestSpecification)
                .body(objectNode)
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .extract().path("token");

        assertThat(token)
                .as("token")
                .isNotEmpty();
    }

    @AfterEach
    void tearDown() {
        RestAssured.reset();
    }
}
