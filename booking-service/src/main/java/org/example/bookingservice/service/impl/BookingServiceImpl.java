package org.example.bookingservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookingservice.domain.Booking;
import org.example.bookingservice.domain.BookingStatus;
import org.example.bookingservice.domain.ServiceName;
import org.example.bookingservice.dto.*;
import org.example.bookingservice.exception.BookingNotFoundException;
import org.example.bookingservice.mapper.BookingMapper;
import org.example.bookingservice.repository.BookingRepository;
import org.example.bookingservice.repository.BookingSpecifications;
import org.example.bookingservice.service.BookingService;
import org.example.bookingservice.service.KafkaService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final KafkaService kafkaService;

    private final BookingMapper bookingMapper = BookingMapper.INSTANCE;

    @Override
    public BookingListResponse getAll(Long userId, boolean isAdmin, Pageable pageable) {
        Specification<Booking> spec = isAdmin ? null : BookingSpecifications.byUser(userId);
        return bookingMapper.bookingListToBookingListResponse(bookingRepository.findAll(spec, pageable));
    }

    @Override
    public BookingResponse getById(Long id) {
        return bookingMapper.bookingToResponse(bookingRepository.findById(id)
                .orElseThrow(() ->
                        new BookingNotFoundException(MessageFormat.format("Booking with id {0} not found", id))));
    }

    @Transactional
    @Override
    public BookingResponse create(Long userId, String email, CreateBookingRequest request) {
        Booking newBooking = bookingMapper.requestToBooking(request);
        newBooking.setUserId(userId);
        newBooking.setStatus(BookingStatus.NEW);
        newBooking.addStatusHistory(newBooking.getStatus(), ServiceName.BOOKING_SERVICE, "Booking created");
        Booking booking = bookingRepository.saveAndFlush(newBooking);
        log.info("Booking with id {} was created at {}", booking.getId(), booking.getCreatedAt());

        kafkaService.produce(HotelKafkaDto.builder()
                .bookingId(booking.getId())
                .userId(userId)
                .roomId(booking.getRoomId())
                .checkIn(booking.getCheckIn())
                .checkOut(booking.getCheckOut())
                .email(email)
                .build());
        return bookingMapper.bookingToResponse(booking);
    }

    @Transactional
    @Override
    public void updateBookingStatus(Long id, StatusDto statusDto) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(MessageFormat.format("Booking with id {0} not found", id)));
        if (booking.getStatus() == statusDto.status()) {
            log.info("Request with same status {} for booking {} from service {}", statusDto.status(), id, statusDto.serviceName());
            return;
        }
        booking.setStatus(statusDto.status());
        booking.addStatusHistory(statusDto.status(), statusDto.serviceName(), statusDto.comment());
        bookingRepository.save(booking);
        log.info("Status for booking with id {} changed to: {}", id, booking.getStatus());
    }
}
