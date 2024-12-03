package io.github.burakkaygusuz.objects;

import java.text.MessageFormat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public record BookingDates(
    @JsonProperty("checkin") String checkIn,
    @JsonProperty("checkout") String checkOut) {

  public BookingDates {
    if (checkIn != null) {
      validateDate(checkIn, "checkin");
    }
    if (checkOut != null) {
      validateDate(checkOut, "checkout");
    }
  }

  private static void validateDate(String date, String fieldName) {
    if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
      throw new IllegalArgumentException(MessageFormat.format("Invalid {0} date format. Use yyyy-MM-dd", fieldName));
    }
  }
}
