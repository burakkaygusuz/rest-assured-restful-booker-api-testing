package io.github.burakkaygusuz.tests;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.burakkaygusuz.objects.Booking;
import io.github.burakkaygusuz.objects.BookingDates;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@Feature("Request Tests")
@DisplayName("Request Tests")
@TestMethodOrder(OrderAnnotation.class)
public class RequestTests extends BaseTest {

    private final Faker faker = new Faker();
    private final String randomFirstName = faker.name().firstName();
    private final String randomLastName = faker.name().lastName();
    protected int bookingId;

    @Test
    @Order(1)
    @DisplayName("Create a new booking")
    @Description("Test Description: Creates a new booking in the API")
    @Story("Create Booking")
    void createNewBooking() {
        Booking booking = new Booking(
                "John",
                "Doe",
                100,
                true,
                new BookingDates("2020-01-01", "2020-01-02"),
                "Breakfast");

        Response response = given()
                .spec(requestSpecification)
                .body(booking)
                .when()
                .post("/booking")
                .then()
                .extract().response();

        bookingId = response.path("bookingid");

        softly.assertThat(response.statusCode()).isEqualTo(200);
        softly.assertThat(bookingId).isNotNull();
        softly.assertAll();
    }

    @Test
    @Order(2)
    @DisplayName("Update the created booking")
    @Description("Test Description: Updates a current booking")
    @Story("Update Booking")
    void updateCreatedBooking() {
        Booking booking = new Booking(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.number().numberBetween(100, 1000),
                false,
                new BookingDates(faker.date().future(1, TimeUnit.DAYS, "YYYY-MM-dd"),
                        faker.date().future(2, TimeUnit.DAYS, "YYYY-MM-dd")),
                "Dinner");

        Response response = given()
                .spec(requestSpecification)
                .body(booking)
                .accept("application/json")
                .when()
                .header("Cookie", "token=" + token)
                .put("/booking/" + bookingId)
                .then()
                .extract().response();

        softly.assertThat(response.statusCode()).as("Status Code").isEqualTo(200);
        softly.assertThat(response.jsonPath().getString("firstname")).isEqualTo(booking.firstName());
        softly.assertThat(response.jsonPath().getString("lastname")).isEqualTo(booking.lastName());
        softly.assertThat(response.jsonPath().getInt("totalprice")).isEqualTo(booking.totalPrice());
        softly.assertThat(response.jsonPath().getBoolean("depositpaid")).isEqualTo(booking.depositPaid());
        softly.assertThat(response.jsonPath().getString("additionalneeds"))
                .isEqualTo(booking.additionalNeeds());
        softly.assertAll();
    }

    @Test
    @Order(3)
    @DisplayName("Update partially the updated booking")
    @Description("Test Description: Updates a current booking with a partial payload")
    @Story("Update Booking")
    void updatePartiallyTheUpdatedBooking() {
        final ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("firstname", randomFirstName);
        node.put("lastname", randomLastName);

        Response response = given()
                .spec(requestSpecification)
                .body(node)
                .accept("application/json")
                .when()
                .header("Cookie", "token=" + token)
                .patch("/booking/" + bookingId)
                .then()
                .extract().response();

        softly.assertThat(response.statusCode()).as("Status Code").isEqualTo(200);
        softly.assertThat(response.jsonPath().getString("firstname")).isEqualTo(node.get("firstname").asText());
        softly.assertThat(response.jsonPath().getString("lastname")).isEqualTo(node.get("lastname").asText());
        softly.assertAll();
    }

    @Test
    @Order(4)
    @DisplayName("Get booking by id")
    @Description("Test Description: Returns a specific booking based upon the booking id provided")
    @Story("Get Booking")
    void getBookingById() {
        Response response = given()
                .spec(requestSpecification)
                .when()
                .get("/booking/" + bookingId)
                .then()
                .extract().response();

        assertThat(response.statusCode())
                .as("Status Code")
                .isEqualTo(200);
    }

    @Test
    @Order(5)
    @DisplayName("Get booking by first name and last name")
    @Description("Test Description: Returns a specific booking based upon the booking firstname and lastname provided")
    @Story("Get Booking")
    void getBookingByFirstNameAndLastName() {
        Response response = given()
                .spec(requestSpecification)
                .queryParam("firstname", randomFirstName)
                .queryParam("lastname", randomLastName)
                .when()
                .get("/booking/" + bookingId)
                .then()
                .extract().response();

        assertThat(response.statusCode())
                .as("Status Code")
                .isEqualTo(200);
    }

    @Test
    @Order(6)
    @DisplayName("Delete booking by id")
    @Description("Test Description: Returns the ids of all the bookings that exist within the API. " +
            "Can take optional query strings to search and return a subset of booking ids")
    @Story("Delete Booking")
    void deleteBookingById() {
        Response response = given()
                .spec(requestSpecification)
                .accept("application/json")
                .when()
                .header("Cookie", "token=" + token)
                .delete("/booking/" + bookingId)
                .then()
                .extract().response();

        assertThat(response.statusCode())
                .as("Status Code")
                .isEqualTo(201);
    }
}
