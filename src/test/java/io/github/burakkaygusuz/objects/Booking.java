package io.github.burakkaygusuz.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Booking(
    @JsonProperty("firstname") String firstName,
    @JsonProperty("lastname") String lastName,
    @JsonProperty("totalprice") @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT) int totalPrice,
    @JsonProperty("depositpaid") @JsonFormat(shape = JsonFormat.Shape.BOOLEAN) boolean depositPaid,
    @JsonProperty("bookingdates") BookingDates bookingDates,
    @JsonProperty("additionalneeds") String additionalNeeds) {
}
