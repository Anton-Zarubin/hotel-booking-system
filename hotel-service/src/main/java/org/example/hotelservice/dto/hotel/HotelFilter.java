package org.example.hotelservice.dto.hotel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelFilter {

    private String city;

    private Integer distanceFromCenter;

    private Double rating;
}
