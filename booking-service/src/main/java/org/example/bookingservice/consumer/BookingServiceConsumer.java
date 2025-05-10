package org.example.bookingservice.consumer;

import org.example.bookingservice.dto.BookingKafkaDto;
import org.example.bookingservice.dto.ErrorKafkaDto;
import org.example.bookingservice.exception.BookingNotFoundException;
import org.example.bookingservice.service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class BookingServiceConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingServiceConsumer.class);

    private final BookingService bookingService;

    @Autowired
    public BookingServiceConsumer(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @KafkaListener(topics = "${spring.kafka.booking-service-topic}",
            containerFactory = "kafkaListenerContainerFactory")
    public void consume(BookingKafkaDto bookingKafkaDto) {
        try {
            LOGGER.info("Consumed message from Kafka -> '{}'", bookingKafkaDto);
            bookingService.updateBookingStatus(bookingKafkaDto.bookingId(), bookingKafkaDto.statusDto());
        } catch (BookingNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    @KafkaListener(topics = "${spring.kafka.error-booking-service-topic}",
            containerFactory = "errorKafkaListenerContainerFactory")
    public void consumeOnFailure(ErrorKafkaDto errorKafkaDto) {
        try {
            LOGGER.info("Consumed an error message from Kafka -> '{}'", errorKafkaDto);
            bookingService.updateBookingStatus(errorKafkaDto.bookingId(), errorKafkaDto.statusDto());
        } catch (BookingNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
}
