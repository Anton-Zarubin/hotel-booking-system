package org.example.hotelservice.dto.hotel;

import java.util.List;

public record HotelListResponse(Long total, List<HotelResponse> hotels) {
}
