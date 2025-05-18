package org.example.notificationservice.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record StatusDto(BookingStatus status, ServiceName serviceName, String comment, LocalDateTime timestamp) {
}
