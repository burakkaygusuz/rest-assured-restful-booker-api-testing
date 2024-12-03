package io.github.burakkaygusuz.tests;

import io.github.burakkaygusuz.services.BookingService;
import io.github.burakkaygusuz.services.BookingServiceImpl;
import io.github.burakkaygusuz.utils.PropertyUtil;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.config;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Restful-Booker API Test Suite")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BaseTest {

  protected final PropertyUtil props = PropertyUtil.getInstance("app.properties");
  protected BookingService bookingService;
  protected String token;

  @BeforeAll
  @Order(1)
  @DisplayName("Health Check")
  @Description("A simple health check endpoint to confirm whether the API is up and running")
  void healthCheck() {

    bookingService = new BookingServiceImpl(getRequestSpecification());
    Response response = bookingService.healthCheck();

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

    token = bookingService.createAuthToken(username, password);

    assertThat(token)
        .as("token")
        .isNotEmpty()
        .isInstanceOf(String.class);
  }

  @AfterAll
  void tearDown() {
    RestAssured.reset();
  }

  private RequestSpecification getRequestSpecification() {
    LogConfig logConfig = LogConfig.logConfig()
        .enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL);

    HttpClientConfig httpClientConfig = HttpClientConfig.httpClientConfig()
        .setParam("http.socket.timeout", 5000)
        .setParam("http.connection.timeout", 5000);

    RestAssured.config = RestAssuredConfig.config()
        .logConfig(logConfig)
        .httpClient(httpClientConfig);

    return new RequestSpecBuilder()
        .setBaseUri(props.getProperty("uri"))
        .setContentType(ContentType.JSON)
        .setConfig(config)
        .setRelaxedHTTPSValidation()
        .build();
  }

}
