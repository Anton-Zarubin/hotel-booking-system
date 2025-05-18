package org.example.hotelservice.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record PaymentKafkaDto(Long bookingId, Long userId, String email, BigDecimal totalCost, LocalDate checkIn) {
}
