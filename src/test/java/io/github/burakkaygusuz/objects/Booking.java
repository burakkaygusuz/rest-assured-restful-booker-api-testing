package io.github.burakkaygusuz.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public record Booking(@JsonProperty("firstname") String firstName,
                      @JsonProperty("lastname") String lastName,
                      @JsonProperty("totalprice") int totalPrice,
                      @JsonProperty("depositpaid") boolean depositPaid,
                      @JsonProperty("bookingdates") BookingDates bookingDates,
                      @JsonProperty("additionalneeds") String additionalNeeds) {
}
