package org.example.notificationservice.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.notificationservice.dto.*;
import org.example.notificationservice.service.KafkaService;
import org.example.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailNotificationService implements NotificationService {

    @Value("${mail.username}")
    private String username;

    private final JavaMailSenderImpl javaMailSender;

    private final KafkaService kafkaService;

    @Transactional
    @Override
    public void send(NotificationKafkaDto notificationKafkaDto) {

        try {
            String message = MessageFormat.format("Booking with id {0} is confirmed.", notificationKafkaDto.bookingId());
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(notificationKafkaDto.email());
            mimeMessageHelper.setFrom(username);
            mimeMessageHelper.setSubject("Booking confirmation");
            mimeMessageHelper.setText(message, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.info("Failed to send notification to {}", notificationKafkaDto.email());
        }

        StatusDto statusDto = createStatusDto(BookingStatus.CONFIRMED, "Booking confirmed");
        kafkaService.produce(new BookingKafkaDto(notificationKafkaDto.bookingId(), statusDto));
    }

    private StatusDto createStatusDto(BookingStatus bookingStatus, String comment) {

        return StatusDto.builder()
                .status(bookingStatus)
                .serviceName(ServiceName.NOTIFICATION_SERVICE)
                .comment(comment)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
