package org.example.paymentservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.paymentservice.domain.Payment;
import org.example.paymentservice.domain.Wallet;
import org.example.paymentservice.dto.*;
import org.example.paymentservice.exception.EntityNotFoundException;
import org.example.paymentservice.exception.InsufficientFundsException;
import org.example.paymentservice.repository.PaymentRepository;
import org.example.paymentservice.repository.WalletRepository;
import org.example.paymentservice.service.KafkaService;
import org.example.paymentservice.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final WalletRepository walletRepository;

    private final PaymentRepository paymentRepository;

    private final KafkaService kafkaService;

    @Transactional
    @Override
    public void pay(PaymentKafkaDto paymentKafkaDto) {
        try {
            Long userId = paymentKafkaDto.userId();
            Long bookingId = paymentKafkaDto.bookingId();
            BigDecimal cost = paymentKafkaDto.totalCost();

            Optional<Wallet> optionalWallet = walletRepository.findByUserId(userId);
            if (optionalWallet.isEmpty()) {
                String comment = MessageFormat.format("Wallet for user with id {0} not found",
                        userId);
                StatusDto statusDto = createStatusDto(BookingStatus.CANCELLED, comment);
                kafkaService.produce(new ErrorKafkaDto(bookingId, statusDto));

                throw new EntityNotFoundException(comment);
            }

            Wallet wallet = optionalWallet.get();
            if (wallet.getBalance().compareTo(cost) < 0) {
                String comment = "Insufficient funds";
                StatusDto statusDto = createStatusDto(BookingStatus.CANCELLED, comment);
                kafkaService.produce(new ErrorKafkaDto(bookingId, statusDto));

                throw new InsufficientFundsException(comment);
            }

            recordFactOfPayment(bookingId, cost, wallet);

            String comment = "Booking paid";
            StatusDto statusDto = createStatusDto(BookingStatus.PAID, comment);
            kafkaService.produce((NotificationKafkaDto.builder()
                    .bookingId(bookingId)
                    .userId(userId))
                    .email(paymentKafkaDto.email())
                    .checkIn(paymentKafkaDto.checkIn())
                    .build());
            kafkaService.produce(new BookingKafkaDto(bookingId, statusDto));

        } catch (Exception ex) {
            if (!(ex instanceof EntityNotFoundException) && !(ex instanceof InsufficientFundsException)) {
                StatusDto statusDto = createStatusDto(BookingStatus.UNEXPECTED_FAILURE, ex.getMessage());
                kafkaService.produce(new ErrorKafkaDto(paymentKafkaDto.bookingId(), statusDto));
            }

            throw new RuntimeException(ex.getMessage());
        }
    }

    private void recordFactOfPayment(Long bookingId, BigDecimal cost, Wallet wallet) {
        wallet.setBalance(wallet.getBalance().subtract(cost));
        walletRepository.save(wallet);

        Payment payment = new Payment();
        payment.setBookingId(bookingId);
        payment.setCost(cost);
        payment.setWallet(wallet);
        paymentRepository.save(payment);
        log.info("Booking with id {} has been paid. The account balance is {}.", bookingId, wallet.getBalance());
    }

    private StatusDto createStatusDto(BookingStatus bookingStatus, String comment) {

        return StatusDto.builder()
                .status(bookingStatus)
                .serviceName(ServiceName.PAYMENT_SERVICE)
                .comment(comment)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
