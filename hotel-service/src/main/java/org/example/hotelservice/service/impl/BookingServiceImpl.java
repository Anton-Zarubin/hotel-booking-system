package org.example.hotelservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.hotelservice.domain.Booking;
import org.example.hotelservice.dto.*;
import org.example.hotelservice.exception.EntityNotFoundException;
import org.example.hotelservice.exception.UnavailableDatesException;
import org.example.hotelservice.repository.BookingRepository;
import org.example.hotelservice.service.BookingService;
import org.example.hotelservice.service.KafkaService;
import org.example.hotelservice.service.RoomService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final RoomService roomService;

    private final KafkaService kafkaService;

    @Transactional
    @Override
    public void book(HotelKafkaDto hotelKafkaDto) {
        try {
            if (bookingRepository.isDatesAvailable(hotelKafkaDto.roomId(), hotelKafkaDto.checkIn(), hotelKafkaDto.checkOut())) {
                Booking booking = Booking.builder()
                        .id(hotelKafkaDto.bookingId())
                        .room(roomService.getRoomById(hotelKafkaDto.roomId()))
                        .checkIn(hotelKafkaDto.checkIn())
                        .checkOut(hotelKafkaDto.checkOut())
                        .build();
                bookingRepository.save(booking);

                StatusDto statusDto = createStatusDto(BookingStatus.PENDING, "The room can be booked. Payment is pending.");
                kafkaService.produce(new BookingKafkaDto(hotelKafkaDto.bookingId(), statusDto));
            } else {
                throw new UnavailableDatesException();
            }
        } catch (Exception ex) {
            StatusDto statusDto;
            if (!(ex instanceof EntityNotFoundException) && !(ex instanceof UnavailableDatesException)) {
                statusDto = createStatusDto(BookingStatus.UNEXPECTED_FAILURE, ex.getMessage());
            } else {
                statusDto = createStatusDto(BookingStatus.CANCELLED, ex.getMessage());
            }
            kafkaService.produce(new ErrorKafkaDto(hotelKafkaDto.bookingId(), statusDto));

            throw new RuntimeException(ex.getMessage());
        }
    }

    @Transactional
    @Override
    public void cancelBooking(ErrorKafkaDto errorKafkaDto) {
        try {
            bookingRepository.deleteById(errorKafkaDto.bookingId());
            kafkaService.produce(errorKafkaDto);
        } catch (Exception ex) {
            StatusDto statusDto = createStatusDto(BookingStatus.UNEXPECTED_FAILURE, ex.getMessage());
            kafkaService.produce(new ErrorKafkaDto(errorKafkaDto.bookingId(), statusDto));
        }
    }

    private StatusDto createStatusDto(BookingStatus bookingStatus, String comment) {

        return StatusDto.builder()
                .status(bookingStatus)
                .serviceName(ServiceName.HOTEL_SERVICE)
                .comment(comment)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
