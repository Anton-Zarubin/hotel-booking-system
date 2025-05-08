package org.example.hotelservice.service;

import org.example.hotelservice.config.TestConfig;
import org.example.hotelservice.domain.Hotel;
import org.example.hotelservice.domain.Room;
import org.example.hotelservice.dto.room.CreateRoomRequest;
import org.example.hotelservice.dto.room.RoomFilter;
import org.example.hotelservice.dto.room.UpdateRoomRequest;
import org.example.hotelservice.repository.HotelRepository;
import org.example.hotelservice.repository.RoomRepository;
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

import java.math.BigDecimal;
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
public class RoomServiceTest {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomService roomService;

    private CreateRoomRequest createRequest;

    private UpdateRoomRequest updateRequest;

    private Hotel hotel;

    private Room room;

    private List<Room> rooms;

    @BeforeEach
    public void setUp() {
        createRequest = new CreateRoomRequest(
                "Standard",
                "Some description",
                21,
                new BigDecimal("8400.00"),
                2,
                1L
        );

        updateRequest = new UpdateRoomRequest(
                "Standard",
                "Some description",
                21,
                new BigDecimal("8400.00"),
                2
        );

        hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Arbat Hotel");
        hotel.setCity("Moscow");
        hotel.setAddress("Plotnikov Lane, 12");
        hotel.setDistanceFromCenter(2100);

        room = new Room();
        room.setName("Standard");
        room.setDescription("some description");
        room.setNumber(21);
        room.setPrice(new BigDecimal("8400.00"));
        room.setCapacity(2);
        room.setHotel(hotel);

        rooms = Collections.singletonList(room);
    }

    @Test
    void getAll() {
        when(roomRepository.findAll(ArgumentMatchers.<Specification<Room>> any(), any(Pageable.class))).thenReturn(new PageImpl<>(rooms));
        assertDoesNotThrow(() -> roomService.getAll(new RoomFilter(), Pageable.ofSize(1)));
    }

    @Test
    void whenExists_thanReturnRoom() {
        when(roomRepository.findById(1L)).thenReturn(Optional.ofNullable(room));
        assertDoesNotThrow(() -> roomService.getById(1L));
    }

    @Test
    void whenRoomNotFound_thanException() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> roomService.getById(1L));
    }

    @Test
    public void create() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.ofNullable(hotel));
        when(roomRepository.save(any(Room.class))).thenReturn(room);
        assertDoesNotThrow(() -> roomService.create(createRequest));
    }

    @Test
    public void update() {
        when(roomRepository.findById(1L)).thenReturn(Optional.ofNullable(room));
        when(roomRepository.save(any(Room.class))).thenReturn(room);
        assertDoesNotThrow(() -> roomService.update(1L, updateRequest));
    }

    @Test
    public void whenRoomNotFound_thanUpdateFailed() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> roomService.update(1L, updateRequest));
    }
}
