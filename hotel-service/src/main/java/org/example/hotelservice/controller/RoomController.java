package org.example.hotelservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.hotelservice.dto.room.*;
import org.example.hotelservice.service.RoomService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    @Operation(summary = "Get all rooms", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/view")
    public ResponseEntity<RoomListResponse> getAllRooms(RoomFilter filter, Pageable pageable) {
        return ResponseEntity.ok()
                .body(roomService.getAll(filter, pageable));
    }

    @Operation(summary = "Get room by id", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/view/{id}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getById(id));
    }

    @Operation(summary = "Add new room", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/add")
    public ResponseEntity<RoomResponse> createRoom(@Valid @RequestBody CreateRoomRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roomService.create(request));
    }

    @Operation(summary = "Update room", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/update/{id}")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long id, @Valid @RequestBody UpdateRoomRequest request) {
        return ResponseEntity.ok(roomService.update(id, request));
    }

    @Operation(summary = "Delete room", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRoomById(@PathVariable Long id) {
        roomService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
