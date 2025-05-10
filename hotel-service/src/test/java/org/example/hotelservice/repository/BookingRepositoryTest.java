package org.example.hotelservice.repository;

import org.example.hotelservice.domain.Booking;
import org.example.hotelservice.domain.Hotel;
import org.example.hotelservice.domain.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
public class BookingRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookingRepository bookingRepositoryJpa;

    private Room room;

    @BeforeEach
    public void setUp() {
        Hotel hotel = new Hotel(
                "Arbat Hotel",
                "Moscow",
                "Plotnikov Lane, 12",
                2100
        );
        entityManager.persist(hotel);

        room = new Room(
                "Standard",
                "Some description",
                21,
                new BigDecimal("8400.00"),
                2,
                hotel
        );
        entityManager.persist(room);

        Booking booking = new Booking(
                1L,
                room,
                LocalDate.now(),
                LocalDate.now().plusDays(2)
        );
        entityManager.persist(booking);
        entityManager.flush();
    }

    @Test
    public void whenDatesAreAvailable_thenReturnTrue() {
        Boolean isAvailable = bookingRepositoryJpa.isDatesAvailable(room.getId(),
                LocalDate.now().plusDays(3), LocalDate.now().plusDays(7));
        assertThat(isAvailable).isTrue();
    }

    @Test
    public void whenDatesAreUnavailable_thenReturnFalse() {
        Boolean isAvailable = bookingRepositoryJpa.isDatesAvailable(room.getId(),
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
        assertThat(isAvailable).isFalse();
    }
}
