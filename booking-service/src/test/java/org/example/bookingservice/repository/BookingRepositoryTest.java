package org.example.bookingservice.repository;

import org.example.bookingservice.domain.Booking;
import org.example.bookingservice.domain.BookingStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
public class BookingRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookingRepository bookingRepositoryJpa;

    @Test
    public void whenGetById_thenReturnBooking() {
        Booking booking = new Booking(
                1L,
                1L,
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                BookingStatus.NEW
        );
        entityManager.persistAndFlush(booking);

        Booking gotBooking = bookingRepositoryJpa.findById(booking.getId()).get();

        assertThat(gotBooking.getStatus())
                .isEqualTo(BookingStatus.NEW);
    }
}
