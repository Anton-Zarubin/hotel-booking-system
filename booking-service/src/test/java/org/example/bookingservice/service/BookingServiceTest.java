package org.example.bookingservice.service;

import org.example.bookingservice.config.TestConfig;
import org.example.bookingservice.domain.Booking;
import org.example.bookingservice.domain.BookingStatus;
import org.example.bookingservice.domain.ServiceName;
import org.example.bookingservice.dto.CreateBookingRequest;
import org.example.bookingservice.dto.StatusDto;
import org.example.bookingservice.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = TestConfig.class)
public class BookingServiceTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingService bookingService;

    private CreateBookingRequest request;

    private Booking booking;

    private List<Booking> bookings;

    private StatusDto statusDto;

    @BeforeEach
    public void setUp() {
        request = new CreateBookingRequest(
                1L,
                LocalDate.now(),
                LocalDate.now().plusDays(2)
        );

        booking = new Booking(
                1L,
                1L,
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                BookingStatus.NEW
        );

        bookings = Collections.singletonList(booking);

        statusDto = new StatusDto(
                BookingStatus.PENDING,
                ServiceName.HOTEL_SERVICE,
                "some comment",
                LocalDateTime.now()
        );
    }

    @Test
    void getAll() {
        when(bookingRepository.findAll(ArgumentMatchers.<Specification<Booking>> any(),  any(Pageable.class)))
                .thenReturn(new PageImpl<>(bookings));
        assertDoesNotThrow(() -> bookingService.getAll(1L, false, Pageable.ofSize(1)));
    }

    @Test
    void whenExists_thenReturnBooking() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.ofNullable(booking));
        assertDoesNotThrow(() -> bookingService.getById(1L));
    }

    @Test
    void whenBookingNotFound_thenException() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> bookingService.getById(1L));
    }

    @Test
    void create() {
        when(bookingRepository.saveAndFlush(any(Booking.class))).thenReturn(booking);
        assertDoesNotThrow(() -> bookingService.create(1L, "user1@test.tst", request));
    }

    @Test
    void updateBookingStatus() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.ofNullable(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        assertDoesNotThrow(() -> bookingService.updateBookingStatus(1L, statusDto));
    }

    @Test
    void updateBookingStatusWithException() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () ->
                bookingService.updateBookingStatus(1L, statusDto));
    }
}
