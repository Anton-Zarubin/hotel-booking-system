package org.example.hotelservice.dto.room;

import java.time.LocalDate;

public record BookingResponse(LocalDate checkIn, LocalDate checkOut) {
}
