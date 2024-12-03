package io.github.burakkaygusuz.services;

import io.github.burakkaygusuz.objects.Booking;
import io.restassured.specification.RequestSpecification;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class BookingServiceImpl implements BookingService {

  private static final String BOOKING_ENDPOINT = "/booking";
  private final RequestSpecification requestSpec;

  public BookingServiceImpl(RequestSpecification requestSpec) {
    this.requestSpec = requestSpec;
  }

  public Response createBooking(Booking booking) {
    return RestAssured.given()
        .spec(requestSpec)
        .body(booking)
        .when()
        .post(BOOKING_ENDPOINT)
        .then()
        .extract()
        .response();
  }

  public Response updateBooking(int bookingId, Booking booking, String token) {
    return RestAssured.given()
        .spec(requestSpec)
        .body(booking)
        .header("Cookie", "token=" + token)
        .when()
        .put(BOOKING_ENDPOINT + "/" + bookingId)
        .then()
        .extract()
        .response();
  }

  public Response partialUpdateBooking(int bookingId, ObjectNode updates, String token) {
    return RestAssured.given()
        .spec(requestSpec)
        .body(updates)
        .header("Cookie", "token=" + token)
        .when()
        .patch(BOOKING_ENDPOINT + "/" + bookingId)
        .then()
        .extract()
        .response();
  }

  public Response getBookingById(int bookingId) {
    return RestAssured.given()
        .spec(requestSpec)
        .when()
        .get(BOOKING_ENDPOINT + "/" + bookingId)
        .then()
        .extract()
        .response();
  }

  public Response getBookingByFirstNameAndLastName(String firstName, String lastName) {
    return RestAssured.given()
        .spec(requestSpec)
        .queryParam("firstname", firstName)
        .queryParam("lastname", lastName)
        .when()
        .get(BOOKING_ENDPOINT)
        .then()
        .extract()
        .response();
  }

  public Response deleteBooking(int bookingId, String token) {
    return RestAssured.given()
        .spec(requestSpec)
        .header("Cookie", "token=" + token)
        .when()
        .delete(BOOKING_ENDPOINT + "/" + bookingId)
        .then()
        .extract()
        .response();
  }

  public Response getAllBookings() {
    return RestAssured.given()
        .spec(requestSpec)
        .when()
        .get(BOOKING_ENDPOINT)
        .then()
        .extract()
        .response();
  }

  public Response getBookingsByDate(String checkin, String checkout) {
    return RestAssured.given()
        .spec(requestSpec)
        .queryParam("checkin", checkin)
        .queryParam("checkout", checkout)
        .when()
        .get(BOOKING_ENDPOINT)
        .then()
        .extract()
        .response();
  }

  public String createAuthToken(String username, String password) {
    final ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
    objectNode.put("username", username)
        .put("password", password);

    Response response = RestAssured.given()
        .spec(requestSpec)
        .body(objectNode)
        .when()
        .post("/auth")
        .then()
        .extract()
        .response();

    return response.path("token");
  }

  public Response healthCheck() {
    return RestAssured.given()
        .spec(requestSpec)
        .when()
        .get("/ping")
        .then()
        .extract()
        .response();
  }
}
