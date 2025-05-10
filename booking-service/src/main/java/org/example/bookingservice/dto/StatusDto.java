package org.example.bookingservice.dto;

import org.example.bookingservice.domain.BookingStatus;
import org.example.bookingservice.domain.ServiceName;

import java.time.LocalDateTime;

public record StatusDto(BookingStatus status, ServiceName serviceName, String comment, LocalDateTime timestamp) {
}
