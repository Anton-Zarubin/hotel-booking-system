package org.example.hotelservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.hotelservice.domain.Room;
import org.example.hotelservice.dto.room.*;
import org.example.hotelservice.exception.EntityNotFoundException;
import org.example.hotelservice.mapper.RoomMapper;
import org.example.hotelservice.repository.RoomRepository;
import org.example.hotelservice.repository.RoomSpecifications;
import org.example.hotelservice.service.HotelService;
import org.example.hotelservice.service.RoomService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@RequiredArgsConstructor
@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    private final HotelService hotelService;

    private final RoomMapper roomMapper = RoomMapper.INSTANCE;

    @Override
    public RoomListResponse getAll(RoomFilter roomFilter, Pageable pageable) {
        return roomMapper.roomListToRoomListResponse(
                roomRepository.findAll(RoomSpecifications.withFilter(roomFilter), pageable)
        );
    }

    @Override
    public RoomResponse getById(Long id) {
        return roomMapper.roomToResponse(roomRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(MessageFormat.format("Room with id {0} not found", id))));
    }

    @Override
    public RoomResponse create(CreateRoomRequest request) {
        Room room = roomMapper.requestToRoom(request);
        room.setHotel(hotelService.getHotelById(request.hotelId()));
        return roomMapper.roomToResponse(roomRepository.save(room));
    }

    @Transactional
    @Override
    public RoomResponse update(Long id, UpdateRoomRequest request) {
        Room room = roomRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Room with id {0} not found", id)));
        roomMapper.update(id, request, room);
        return roomMapper.roomToResponse(roomRepository.save(room));
    }

    @Override
    public void deleteById(Long id) {
        roomRepository.deleteById(id);
    }
}
