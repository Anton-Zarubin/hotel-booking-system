package org.example.notificationservice.consumer;

import org.example.notificationservice.dto.NotificationKafkaDto;
import org.example.notificationservice.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationServiceConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationServiceConsumer.class);

    private final NotificationService notificationService;

    @Autowired
    public NotificationServiceConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "${spring.kafka.notification-service-topic}")
    public void consumeFromPaymentService(NotificationKafkaDto notificationKafkaDto) {
        LOGGER.info("Consumed message from Kafka -> '{}'", notificationKafkaDto);
        notificationService.send(notificationKafkaDto);
    }
}
