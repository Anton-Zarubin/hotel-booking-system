package org.example.paymentservice.dto;

import java.time.LocalDateTime;

public record ErrorResponse(String message, LocalDateTime timestamp) {
}
