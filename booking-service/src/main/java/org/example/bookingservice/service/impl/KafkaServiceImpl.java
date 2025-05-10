package org.example.bookingservice.service.impl;

import org.example.bookingservice.dto.HotelKafkaDto;
import org.example.bookingservice.service.KafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaServiceImpl implements KafkaService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaServiceImpl.class);

    @Value("${spring.kafka.hotel-service-topic}")
    private String kafkaTopic;

    private final KafkaTemplate<String, HotelKafkaDto> kafkaTemplate;

    public KafkaServiceImpl(KafkaTemplate<String, HotelKafkaDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void produce(HotelKafkaDto hotelKafkaDto) {
        kafkaTemplate.send(kafkaTopic, hotelKafkaDto);
        logger.info("Sent message to Kafka -> '{}'", hotelKafkaDto);
    }
}
