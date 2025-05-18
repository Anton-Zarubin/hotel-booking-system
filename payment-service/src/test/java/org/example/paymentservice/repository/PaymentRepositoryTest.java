package org.example.paymentservice.repository;

import org.example.paymentservice.domain.Payment;
import org.example.paymentservice.domain.Wallet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@DataJpaTest
public class PaymentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PaymentRepository paymentRepositoryJpa;

    @Test
    public void whenGetByBookingId_thenReturnPayment() {
        Wallet wallet = new Wallet();
        wallet.setUserId(1L);
        wallet.setBalance(BigDecimal.ZERO);

        entityManager.persist(wallet);

        Payment payment = new Payment();
        payment.setBookingId(1L);
        payment.setCost(BigDecimal.TEN);
        payment.setWallet(wallet);

        entityManager.persist(payment);
        entityManager.flush();

        Payment gotPayment = paymentRepositoryJpa.findByBookingId(payment.getBookingId()).get();

        assertThat(gotPayment.getCost())
                .isEqualTo(payment.getCost());
    }
}
