package org.example.paymentservice.dto;

import java.math.BigDecimal;

public record PaymentKafkaDto(Long bookingId, Long userId, String email, BigDecimal totalCost) {
}
