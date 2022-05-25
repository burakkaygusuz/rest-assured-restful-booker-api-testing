package io.github.burakkaygusuz.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public record BookingDates(@JsonProperty("checkin") String checkIn,
                           @JsonProperty("checkout") String checkOut) {
}
