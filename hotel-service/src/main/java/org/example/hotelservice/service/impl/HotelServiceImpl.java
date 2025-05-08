package org.example.hotelservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.hotelservice.domain.Hotel;
import org.example.hotelservice.dto.hotel.HotelFilter;
import org.example.hotelservice.dto.hotel.HotelListResponse;
import org.example.hotelservice.dto.hotel.HotelResponse;
import org.example.hotelservice.dto.hotel.UpsertHotelRequest;
import org.example.hotelservice.exception.EntityNotFoundException;
import org.example.hotelservice.mapper.HotelMapper;
import org.example.hotelservice.repository.HotelRepository;
import org.example.hotelservice.repository.HotelSpecifications;
import org.example.hotelservice.service.HotelService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@RequiredArgsConstructor
@Service
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;

    private final HotelMapper hotelMapper = HotelMapper.INSTANCE;

    @Override
    public HotelListResponse getAll(HotelFilter hotelFilter, Pageable pageable) {
        return hotelMapper.hotelListToHotelListResponse(
                hotelRepository.findAll(HotelSpecifications.withFilter(hotelFilter), pageable)
        );
    }

    @Override
    public Hotel getHotelById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(MessageFormat.format("Hotel with id {0} not found", id)));
    }

    @Override
    public HotelResponse getById(Long id) {
        return hotelMapper.hotelToResponse(getHotelById(id));
    }

    @Override
    public HotelResponse create(UpsertHotelRequest request) {
        return hotelMapper.hotelToResponse(hotelRepository.save(hotelMapper.requestToHotel(request)));
    }

    @Transactional
    @Override
    public HotelResponse update(Long id, UpsertHotelRequest request) {
        Hotel hotel = getHotelById(id);
        hotelMapper.update(id, request, hotel);
        return hotelMapper.hotelToResponse(hotelRepository.save(hotel));
    }

    @Override
    public void deleteById(Long id) {
        hotelRepository.deleteById(id);
    }

    @Transactional
    @Override
    public HotelResponse updateRating(Long id, Integer newMark) {
        Hotel hotel = getHotelById(id);
        double rating = hotel.getRating();
        int numberOfRating = hotel.getNumberOfRating();

        double totalRating = rating * numberOfRating;
        totalRating = totalRating - rating + newMark;
        rating = numberOfRating != 0 ? totalRating / numberOfRating : totalRating;

        hotel.setRating(Math.round(rating * 10.) / 10.);
        hotel.setNumberOfRating(numberOfRating + 1);

        return hotelMapper.hotelToResponse(hotelRepository.save(hotel));
    }
}
