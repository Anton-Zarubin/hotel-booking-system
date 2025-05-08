package org.example.hotelservice.mapper;

import org.example.hotelservice.domain.Hotel;
import org.example.hotelservice.dto.hotel.HotelListResponse;
import org.example.hotelservice.dto.hotel.HotelResponse;
import org.example.hotelservice.dto.hotel.UpsertHotelRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HotelMapper {

    HotelMapper INSTANCE = Mappers.getMapper(HotelMapper.class);

    HotelResponse hotelToResponse(Hotel hotel);

    Hotel requestToHotel(UpsertHotelRequest request);

    void update(Long id, UpsertHotelRequest request, @MappingTarget Hotel hotel);

    List<HotelResponse> hotelListToResponseList(List<Hotel> hotels);

    default HotelListResponse hotelListToHotelListResponse(Page<Hotel> hotels) {
        return new HotelListResponse(hotels.getTotalElements(), hotelListToResponseList(hotels.getContent()));
    }
}
