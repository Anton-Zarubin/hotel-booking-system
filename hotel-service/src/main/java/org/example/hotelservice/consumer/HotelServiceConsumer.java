package org.example.hotelservice.consumer;

import org.example.hotelservice.dto.ErrorKafkaDto;
import org.example.hotelservice.dto.HotelKafkaDto;
import org.example.hotelservice.service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class HotelServiceConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(HotelServiceConsumer.class);

    private final BookingService bookingService;

    @Autowired
    public HotelServiceConsumer(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @KafkaListener(topics = "${spring.kafka.hotel-service-topic}")
    public void consumeFromBookingService(HotelKafkaDto hotelKafkaDto) {
        LOGGER.info("Consumed message from Kafka -> '{}'", hotelKafkaDto);
        bookingService.book(hotelKafkaDto);
    }

    @KafkaListener(topics = "${spring.kafka.error-hotel-service-topic}",
            containerFactory = "ErrorKafkaListenerContainerFactory")
    public void consumeFromPaymentService(ErrorKafkaDto errorKafkaDto) {
        LOGGER.info("Consumed an error message from Kafka -> '{}'", errorKafkaDto);
        bookingService.cancelBooking(errorKafkaDto);
    }
}
