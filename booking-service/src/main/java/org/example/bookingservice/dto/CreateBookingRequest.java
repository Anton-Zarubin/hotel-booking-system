package org.example.bookingservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateBookingRequest(@NotNull(message = "Room id is required") Long roomId,
                                   @FutureOrPresent(message = "Incorrect check-in date") LocalDate checkIn,
                                   @Future(message = "Incorrect check-out date") LocalDate checkOut) {
}
