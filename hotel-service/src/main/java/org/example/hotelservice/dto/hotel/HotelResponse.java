package org.example.hotelservice.dto.hotel;

public record HotelResponse(Long id,
                            String name,
                            String city,
                            String address,
                            Integer distanceFromCenter,
                            double rating,
                            int numberOfRating) {
}
