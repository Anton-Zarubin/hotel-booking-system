package org.example.hotelservice.dto.room;

import java.util.List;

public record RoomListResponse(Long total, List<RoomResponse> rooms) {
}
