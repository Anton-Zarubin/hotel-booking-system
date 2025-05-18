package org.example.paymentservice.service;

import org.example.paymentservice.config.TestConfig;
import org.example.paymentservice.domain.Wallet;
import org.example.paymentservice.dto.ErrorKafkaDto;
import org.example.paymentservice.dto.PaymentKafkaDto;
import org.example.paymentservice.repository.PaymentRepository;
import org.example.paymentservice.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = TestConfig.class)
public class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private WalletRepository walletRepository;

    private PaymentKafkaDto paymentKafkaDto;

    private ErrorKafkaDto errorKafkaDto;

    @BeforeEach
    public void setUp() {
        paymentKafkaDto = new PaymentKafkaDto(1L, 1L, "user1@test.tst", BigDecimal.TEN, LocalDate.now());
    }

    @Test
    void pay() {
        Wallet wallet = new Wallet();
        wallet.setUserId(1L);
        wallet.setBalance(BigDecimal.TEN);

        when(walletRepository.findByUserId(1L)).thenReturn(Optional.of(wallet));
        assertDoesNotThrow(() -> paymentService.pay(paymentKafkaDto));
        assertThat(wallet.getBalance()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void payWithException() {
        when(walletRepository.findByUserId(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> paymentService.pay(paymentKafkaDto));
    }
}
