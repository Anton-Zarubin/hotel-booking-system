package org.example.paymentservice.config;

import org.example.paymentservice.repository.PaymentRepository;
import org.example.paymentservice.repository.WalletRepository;
import org.example.paymentservice.service.KafkaService;
import org.example.paymentservice.service.PaymentService;
import org.example.paymentservice.service.WalletService;
import org.example.paymentservice.service.impl.PaymentServiceImpl;
import org.example.paymentservice.service.impl.WalletServiceImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestConfig {

    @Bean
    public WalletRepository walletRepository(){
        return mock(WalletRepository.class);
    }

    @Bean
    public PaymentRepository paymentRepository() {
        return mock(PaymentRepository.class);
    }

    @Bean
    public KafkaService kafkaService() {
        return mock(KafkaService.class);
    }

    @Bean
    public WalletService walletService() {
        return new WalletServiceImpl(walletRepository());
    }

    @Bean
    public PaymentService paymentService() {
        return new PaymentServiceImpl(walletRepository(), paymentRepository(), kafkaService());
    }
}
