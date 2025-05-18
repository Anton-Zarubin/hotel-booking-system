package org.example.paymentservice.consumer;

import org.example.paymentservice.dto.PaymentKafkaDto;
import org.example.paymentservice.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentServiceConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceConsumer.class);

    private final PaymentService paymentService;

    @Autowired
    public PaymentServiceConsumer(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @KafkaListener(topics = "${spring.kafka.payment-service-topic}")
    public void consumeFromHotelService(PaymentKafkaDto paymentKafkaDto) {
        LOGGER.info("Consumed message from Kafka -> '{}'", paymentKafkaDto);
        paymentService.pay(paymentKafkaDto);
    }
}
