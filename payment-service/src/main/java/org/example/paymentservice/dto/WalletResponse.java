package org.example.paymentservice.dto;

import java.math.BigDecimal;

public record WalletResponse(Long id, Long userId, BigDecimal balance) {
}
