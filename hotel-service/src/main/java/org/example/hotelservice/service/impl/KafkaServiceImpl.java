package org.example.hotelservice.service.impl;

import org.example.hotelservice.dto.ErrorKafkaDto;
import org.example.hotelservice.dto.PaymentKafkaDto;
import org.example.hotelservice.service.KafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaServiceImpl implements KafkaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaServiceImpl.class);

    @Value("${spring.kafka.error-booking-service-topic}")
    private String kafkaErrorBookingServiceTopic;

    @Value("${spring.kafka.booking-service-topic}")
    private String kafkaBookingServiceTopic;

    @Value("${spring.kafka.payment-service-topic}")
    private String kafkaPaymentServiceTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaServiceImpl(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void produce(Object kafkaDto) {
        if (kafkaDto instanceof PaymentKafkaDto) {
            kafkaTemplate.send(kafkaPaymentServiceTopic, kafkaDto);
        } else if (kafkaDto instanceof ErrorKafkaDto) {
            kafkaTemplate.send(kafkaErrorBookingServiceTopic, kafkaDto);
        } else {
            kafkaTemplate.send(kafkaBookingServiceTopic, kafkaDto);
        }
        LOGGER.info("Sent message to Kafka -> '{}'", kafkaDto);
    }
}
