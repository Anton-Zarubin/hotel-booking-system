package org.example.hotelservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.example.hotelservice.dto.hotel.HotelFilter;
import org.example.hotelservice.dto.hotel.HotelListResponse;
import org.example.hotelservice.dto.hotel.HotelResponse;
import org.example.hotelservice.dto.hotel.UpsertHotelRequest;
import org.example.hotelservice.service.HotelService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/hotels")
public class HotelController {

    private final HotelService hotelService;

    @Operation(summary = "Get all hotels", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/view")
    public ResponseEntity<HotelListResponse> getAllHotels(HotelFilter filter, Pageable pageable) {
        return ResponseEntity.ok()
                .body(hotelService.getAll(filter, pageable));
    }

    @Operation(summary = "Get hotel by id", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/view/{id}")
    public ResponseEntity<HotelResponse> getHotelById(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.getById(id));
    }

    @Operation(summary = "Add new hotel", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/add")
    public ResponseEntity<HotelResponse> createHotel(@Valid @RequestBody UpsertHotelRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(hotelService.create(request));
    }

    @Operation(summary = "Update hotel", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/update/{id}")
    public ResponseEntity<HotelResponse> updateHotel(@PathVariable Long id, @Valid @RequestBody UpsertHotelRequest request) {
        return ResponseEntity.ok(hotelService.update(id, request));
    }

    @Operation(summary = "Delete hotel", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteHotelById(@PathVariable Long id) {
        hotelService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Rate hotel", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/rate/{id}")
    public ResponseEntity<HotelResponse> updateHotelRatingById(@PathVariable Long id,
                                                               @Min(1) @Max(5) @RequestParam int newMark) {
        return ResponseEntity.ok(hotelService.updateRating(id, newMark));
    }
}
