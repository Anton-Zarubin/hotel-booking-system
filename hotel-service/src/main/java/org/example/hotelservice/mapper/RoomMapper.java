package org.example.hotelservice.mapper;

import org.example.hotelservice.domain.Room;
import org.example.hotelservice.dto.room.CreateRoomRequest;
import org.example.hotelservice.dto.room.RoomListResponse;
import org.example.hotelservice.dto.room.RoomResponse;
import org.example.hotelservice.dto.room.UpdateRoomRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomMapper {

    RoomMapper INSTANCE = Mappers.getMapper(RoomMapper.class);

    RoomResponse roomToResponse(Room room);

    Room requestToRoom(CreateRoomRequest request);

    void update(Long id, UpdateRoomRequest request, @MappingTarget Room room);

    List<RoomResponse> roomListToResponseList(List<Room> rooms);

    default RoomListResponse roomListToRoomListResponse(Page<Room> rooms) {
        return new RoomListResponse(rooms.getTotalElements(), roomListToResponseList(rooms.getContent()));
    }
}
