package org.example.hotelservice.config;

import org.example.hotelservice.repository.BookingRepository;
import org.example.hotelservice.repository.HotelRepository;
import org.example.hotelservice.repository.RoomRepository;
import org.example.hotelservice.service.BookingService;
import org.example.hotelservice.service.HotelService;
import org.example.hotelservice.service.KafkaService;
import org.example.hotelservice.service.RoomService;
import org.example.hotelservice.service.impl.BookingServiceImpl;
import org.example.hotelservice.service.impl.HotelServiceImpl;
import org.example.hotelservice.service.impl.RoomServiceImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestConfig {

    @Bean
    public HotelRepository hotelRepository() {
        return mock(HotelRepository.class);
    }

    @Bean
    public RoomRepository roomRepository() {
        return mock(RoomRepository.class);
    }

    @Bean
    public BookingRepository bookingRepository(){
        return mock(BookingRepository.class);
    }

    @Bean
    public KafkaService kafkaService() {
        return mock(KafkaService.class);
    }

    @Bean
    public HotelService hotelService() {
        return new HotelServiceImpl(hotelRepository());
    }

    @Bean
    public RoomService roomService() {
        return new RoomServiceImpl(roomRepository(), hotelService());
    }

    @Bean
    public BookingService bookingService() {
        return new BookingServiceImpl(bookingRepository(), roomService(), kafkaService());
    }
}
