package org.example.hotelservice.service;

import org.example.hotelservice.config.TestConfig;
import org.example.hotelservice.domain.Room;
import org.example.hotelservice.dto.HotelKafkaDto;
import org.example.hotelservice.repository.BookingRepository;
import org.example.hotelservice.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = TestConfig.class)
public class BookingServiceTest {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingService bookingService;

    private HotelKafkaDto hotelKafkaDto;

    @BeforeEach
    public void setUp() {
        hotelKafkaDto = HotelKafkaDto.builder()
                .bookingId(1L)
                .roomId(1L)
                .checkIn(LocalDate.now())
                .checkOut(LocalDate.now().plusDays(2))
                .email("user1@test.tst")
                .build();
    }

    @Test
    void book() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(new Room()));
        when(bookingRepository.isDatesAvailable(anyLong(), any(LocalDate.class),any(LocalDate.class))).thenReturn(true);
        assertDoesNotThrow(() -> bookingService.book(hotelKafkaDto));
    }

    @Test
    void bookWithException() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> bookingService.book(hotelKafkaDto));
    }
}
