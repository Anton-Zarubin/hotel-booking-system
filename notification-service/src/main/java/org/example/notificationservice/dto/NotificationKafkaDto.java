package org.example.notificationservice.dto;

import java.time.LocalDate;

public record NotificationKafkaDto(Long bookingId, Long userId, String email, LocalDate checkIn) {
}
