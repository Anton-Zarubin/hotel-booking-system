package org.example.paymentservice.dto;

import lombok.Builder;

@Builder
public record NotificationKafkaDto(Long bookingId, Long userId, String email) {
}
