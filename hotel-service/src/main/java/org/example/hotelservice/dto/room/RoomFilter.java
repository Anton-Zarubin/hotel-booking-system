package org.example.hotelservice.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomFilter {

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    private Integer capacity;

    private Long hotelId;

    private LocalDate checkIn;

    private LocalDate checkOut;
}
