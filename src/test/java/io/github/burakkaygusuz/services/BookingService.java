package io.github.burakkaygusuz.services;

import com.fasterxml.jackson.databind.node.ObjectNode;

import io.github.burakkaygusuz.objects.Booking;
import io.restassured.response.Response;

public interface BookingService {

  Response createBooking(Booking booking);

  Response updateBooking(int bookingId, Booking booking, String token);

  Response partialUpdateBooking(int bookingId, ObjectNode updates, String token);

  Response getBookingById(int bookingId);

  Response getBookingByFirstNameAndLastName(String firstName, String lastName);

  Response deleteBooking(int bookingId, String token);

  Response getAllBookings();

  Response getBookingsByDate(String checkin, String checkout);

  String createAuthToken(String username, String password);

  Response healthCheck();
}
