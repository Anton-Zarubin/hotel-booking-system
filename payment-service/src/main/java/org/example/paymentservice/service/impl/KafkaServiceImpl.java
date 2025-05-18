package org.example.paymentservice.service.impl;

import org.example.paymentservice.dto.ErrorKafkaDto;
import org.example.paymentservice.dto.NotificationKafkaDto;
import org.example.paymentservice.service.KafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaServiceImpl implements KafkaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaServiceImpl.class);

    @Value("${spring.kafka.error-hotel-service-topic}")
    private String kafkaErrorHotelServiceTopic;

    @Value("${spring.kafka.booking-service-topic}")
    private String kafkaBookingServiceTopic;

    @Value("${spring.kafka.notification-service-topic}")
    private String kafkaNotificationServiceTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaServiceImpl(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void produce(Object kafkaDto) {
        if (kafkaDto instanceof NotificationKafkaDto) {
            kafkaTemplate.send(kafkaNotificationServiceTopic, kafkaDto);
        } else if (kafkaDto instanceof ErrorKafkaDto) {
            kafkaTemplate.send(kafkaErrorHotelServiceTopic, kafkaDto);
        } else {
            kafkaTemplate.send(kafkaBookingServiceTopic, kafkaDto);
        }
        LOGGER.info("Sent message to Kafka -> '{}'", kafkaDto);
    }
}
