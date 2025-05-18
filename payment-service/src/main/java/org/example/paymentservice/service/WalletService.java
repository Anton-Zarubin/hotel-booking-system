package org.example.paymentservice.service;

import org.example.paymentservice.dto.ReplenishmentRequest;
import org.example.paymentservice.dto.WalletResponse;

import java.math.BigDecimal;

public interface WalletService {

    WalletResponse createWallet(Long userId);

    BigDecimal getBalance(Long userId);

    BigDecimal replenishBalance(Long userId, ReplenishmentRequest replenishmentRequest);
}
