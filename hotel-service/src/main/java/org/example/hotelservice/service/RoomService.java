package org.example.hotelservice.service;

import org.example.hotelservice.dto.room.*;
import org.springframework.data.domain.Pageable;

public interface RoomService {

    RoomListResponse getAll(RoomFilter roomFilter, Pageable pageable);

    RoomResponse getById(Long id);

    RoomResponse create(CreateRoomRequest request);

    RoomResponse update(Long id, UpdateRoomRequest request);

    void deleteById(Long id);
}
