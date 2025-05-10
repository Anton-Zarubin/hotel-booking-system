package org.example.bookingservice.dto;

import org.example.bookingservice.domain.BookingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record BookingResponse(Long id,
                              Long userId,
                              Long roomId,
                              LocalDate checkIn,
                              LocalDate checkOut,
                              BookingStatus status,
                              List<StatusDto> history,
                              LocalDateTime createdAt,
                              LocalDateTime modifiedAt) {
}
