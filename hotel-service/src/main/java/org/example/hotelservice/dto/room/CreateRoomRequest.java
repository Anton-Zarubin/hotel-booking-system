package org.example.hotelservice.dto.room;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;

public record CreateRoomRequest(@NotBlank(message = "Name must not be blank") String name,
                                @NotBlank(message = "Description is required") String description,
                                @Positive(message = "Room number must be positive") int number,
                                @Positive(message = "Price must be positive") BigDecimal price,
                                @Range(min = 1, max = 12, message = "Room capacity cannot be less than 1 and more than 12 people") int capacity,
                                @NotNull(message = "Hotel id must not be null") Long hotelId) {
}
