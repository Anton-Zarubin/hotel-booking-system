package org.example.paymentservice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentKafkaDto(Long bookingId, Long userId, String email, BigDecimal totalCost, LocalDate checkIn) {
}
