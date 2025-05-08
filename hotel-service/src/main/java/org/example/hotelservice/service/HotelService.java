package org.example.hotelservice.service;

import org.example.hotelservice.domain.Hotel;
import org.example.hotelservice.dto.hotel.HotelFilter;
import org.example.hotelservice.dto.hotel.HotelListResponse;
import org.example.hotelservice.dto.hotel.HotelResponse;
import org.example.hotelservice.dto.hotel.UpsertHotelRequest;
import org.springframework.data.domain.Pageable;

public interface HotelService {

    HotelListResponse getAll(HotelFilter hotelFilter, Pageable pageable);

    Hotel getHotelById(Long id);

    HotelResponse getById(Long id);

    HotelResponse create(UpsertHotelRequest request);

    HotelResponse update(Long id, UpsertHotelRequest request);

    void deleteById(Long id);

    HotelResponse updateRating(Long id, Integer newMark);
}
