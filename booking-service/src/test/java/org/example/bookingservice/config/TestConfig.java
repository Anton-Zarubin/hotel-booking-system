package org.example.bookingservice.config;

import org.example.bookingservice.repository.BookingRepository;
import org.example.bookingservice.service.BookingService;
import org.example.bookingservice.service.KafkaService;
import org.example.bookingservice.service.impl.BookingServiceImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestConfig {

    @Bean
    public BookingRepository bookingRepository(){
        return mock(BookingRepository.class);
    }

    @Bean
    public KafkaService kafkaService() {
        return mock(KafkaService.class);
    }

    @Bean
    public BookingService bookingService() {
        return new BookingServiceImpl(bookingRepository(), kafkaService());
    }
}
