package org.example.bookingservice.service;

import org.example.bookingservice.dto.BookingListResponse;
import org.example.bookingservice.dto.CreateBookingRequest;
import org.example.bookingservice.dto.BookingResponse;
import org.example.bookingservice.dto.StatusDto;
import org.springframework.data.domain.Pageable;

public interface BookingService {

    BookingListResponse getAll(Long userId, boolean isAdmin, Pageable pageable);

    BookingResponse getById(Long id);

    BookingResponse create(Long userId, String email, CreateBookingRequest request);

    void updateBookingStatus(Long id, StatusDto statusDto);
}
