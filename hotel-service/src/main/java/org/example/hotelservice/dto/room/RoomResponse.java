package org.example.hotelservice.dto.room;

import org.example.hotelservice.dto.hotel.HotelResponse;

import java.math.BigDecimal;
import java.util.List;

public record RoomResponse(Long id,
                           String name,
                           String description,
                           int number,
                           BigDecimal price,
                           int capacity,
                           HotelResponse hotel,
                           List<BookingResponse>bookings) {
}
