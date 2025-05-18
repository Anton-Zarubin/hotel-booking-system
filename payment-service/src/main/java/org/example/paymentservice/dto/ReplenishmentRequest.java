package org.example.paymentservice.dto;

import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

public record ReplenishmentRequest(@Min(value = 1, message = "The amount for replenishment cannot be less than 1")
                                   BigDecimal amount) {
}
