package org.example.bookingservice.dto;

import java.util.List;

public record BookingListResponse(Long total, List<BookingResponse> bookings) {
}
