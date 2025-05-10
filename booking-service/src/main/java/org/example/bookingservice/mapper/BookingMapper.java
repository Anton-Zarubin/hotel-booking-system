package org.example.bookingservice.mapper;

import org.example.bookingservice.domain.Booking;
import org.example.bookingservice.dto.BookingListResponse;
import org.example.bookingservice.dto.CreateBookingRequest;
import org.example.bookingservice.dto.BookingResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(uses = BookingStatusMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookingMapper {

    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Mapping(target = "history", source = "bookingStatusHistory")
    BookingResponse bookingToResponse(Booking booking);

    Booking requestToBooking(CreateBookingRequest request);

    List<BookingResponse> bookingListToResponseList(List<Booking> bookings);

    default BookingListResponse bookingListToBookingListResponse(Page<Booking> bookings) {
        return new BookingListResponse(bookings.getTotalElements(), bookingListToResponseList(bookings.getContent()));
    }
}
