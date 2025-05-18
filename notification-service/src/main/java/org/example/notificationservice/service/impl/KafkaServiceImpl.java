package org.example.notificationservice.service.impl;

import org.example.notificationservice.dto.BookingKafkaDto;
import org.example.notificationservice.service.KafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaServiceImpl implements KafkaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaServiceImpl.class);

    @Value("${spring.kafka.booking-service-topic}")
    private String kafkaBookingServiceTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public KafkaServiceImpl(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void produce(BookingKafkaDto bookingKafkaDto) {
        kafkaTemplate.send(kafkaBookingServiceTopic, bookingKafkaDto);
        LOGGER.info("Sent message to Kafka -> '{}'", bookingKafkaDto);
    }
}
