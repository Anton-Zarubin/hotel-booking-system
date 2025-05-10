package org.example.hotelservice.dto;

import java.math.BigDecimal;

public record PaymentKafkaDto(Long bookingId, Long userId, String email, BigDecimal totalCost) {
}
