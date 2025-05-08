package org.example.hotelservice.service;

import org.example.hotelservice.config.TestConfig;
import org.example.hotelservice.domain.Hotel;
import org.example.hotelservice.dto.hotel.HotelFilter;
import org.example.hotelservice.dto.hotel.UpsertHotelRequest;
import org.example.hotelservice.repository.HotelRepository;
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
public class HotelServiceTest {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private HotelService hotelService;

    private UpsertHotelRequest request;

    private Hotel hotel;

    private List<Hotel> hotels;

    @BeforeEach
    public void setUp() {
        request = new UpsertHotelRequest(
                "Arbat Hotel",
                "Moscow",
                "Plotnikov Lane, 12",
                2100
        );

        hotel = new Hotel();
        hotel.setName("Arbat Hotel");
        hotel.setCity("Moscow");
        hotel.setAddress("Plotnikov Lane, 12");
        hotel.setDistanceFromCenter(2100);

        hotels = Collections.singletonList(hotel);
    }

    @Test
    void getAll() {
        when(hotelRepository.findAll(ArgumentMatchers.<Specification<Hotel>> any(), any(Pageable.class))).thenReturn(new PageImpl<>(hotels));
        assertDoesNotThrow(() -> hotelService.getAll(new HotelFilter(), Pageable.ofSize(1)));
    }

    @Test
    void whenExists_thanReturnHotel() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.ofNullable(hotel));
        assertDoesNotThrow(() -> hotelService.getById(1L));
    }

    @Test
    void whenHotelNotFound_thanException() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> hotelService.getById(1L));
    }

    @Test
    public void create() {
        when(hotelRepository.save(any(Hotel.class))).thenReturn(hotel);
        assertDoesNotThrow(() -> hotelService.create(request));
    }

    @Test
    public void update() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.ofNullable(hotel));
        when(hotelRepository.save(any(Hotel.class))).thenReturn(hotel);
        assertDoesNotThrow(() -> hotelService.update(1L, request));
    }

    @Test
    public void whenHotelNotFound_thanUpdateFailed() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> hotelService.update(1L, request));
    }
}
