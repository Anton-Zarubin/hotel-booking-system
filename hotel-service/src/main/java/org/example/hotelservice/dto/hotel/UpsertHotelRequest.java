package org.example.hotelservice.dto.hotel;

import jakarta.validation.constraints.NotBlank;

public record UpsertHotelRequest(@NotBlank(message = "Name must not be blank") String name,
                                 @NotBlank(message = "City is required") String city,
                                 @NotBlank(message = "Address is required") String address,
                                 Integer distanceFromCenter) {
}
