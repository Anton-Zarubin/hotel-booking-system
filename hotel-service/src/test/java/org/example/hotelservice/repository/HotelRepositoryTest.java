package org.example.hotelservice.repository;

import org.example.hotelservice.domain.Hotel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
public class HotelRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private HotelRepository hotelRepositoryJpa;

    @Test
    public void whenGetById_thenReturnHotel() {
        Hotel hotel = new Hotel(
                "Arbat Hotel",
                "Moscow",
                "Plotnikov Lane, 12",
                2100
        );
        entityManager.persist(hotel);
        entityManager.flush();

        Hotel gotHotel = hotelRepositoryJpa.findById(hotel.getId()).get();

        assertThat(gotHotel.getName())
                .isEqualTo(hotel.getName());
    }
}
