package org.example.hotelservice.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record HotelKafkaDto(Long bookingId,
                            Long userId,
                            Long roomId,
                            LocalDate checkIn,
                            LocalDate checkOut,
                            String email) {
}
