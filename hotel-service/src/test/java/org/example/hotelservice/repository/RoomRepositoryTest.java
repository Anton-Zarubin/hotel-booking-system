package org.example.hotelservice.repository;

import org.example.hotelservice.domain.Hotel;
import org.example.hotelservice.domain.Room;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
public class RoomRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoomRepository roomRepositoryJpa;

    @Test
    public void whenGetById_thenReturnRoom() {
        Hotel hotel = new Hotel(
                "Arbat Hotel",
                "Moscow",
                "Plotnikov Lane, 12",
                2100
        );
        entityManager.persist(hotel);

        Room room = new Room(
                "Standard",
                "Some description",
                21,
                new BigDecimal("8400.00"),
                2,
                hotel
        );
        entityManager.persist(room);
        entityManager.flush();

        Room gotRoom = roomRepositoryJpa.findById(room.getId()).get();

        assertThat(gotRoom.getName())
                .isEqualTo(room.getName());
    }
}
