package org.example.bookingservice.mapper;

import org.example.bookingservice.domain.BookingStatusHistory;
import org.example.bookingservice.dto.StatusDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookingStatusMapper {

    BookingStatusMapper INSTANCE = Mappers.getMapper(BookingStatusMapper.class);

    StatusDto statusToDto(BookingStatusHistory bookingStatusHistory);

    List<StatusDto> statusListToDtoList(List<BookingStatusHistory> bookingStatuses);
}
