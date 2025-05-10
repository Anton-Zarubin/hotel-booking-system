package org.example.bookingservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookingservice.dto.BookingListResponse;
import org.example.bookingservice.dto.BookingResponse;
import org.example.bookingservice.dto.CreateBookingRequest;
import org.example.bookingservice.exception.AccessDeniedException;
import org.example.bookingservice.service.BookingService;
import org.example.bookingservice.utils.RequestHeaderUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Operation(summary = "Get all bookings in system", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping()
    public ResponseEntity<BookingListResponse> getAllBookings(HttpServletRequest request, Pageable pageable) {
        return ResponseEntity.ok(bookingService.getAll(RequestHeaderUtils.getActiveUserId(request),
                RequestHeaderUtils.isAdmin(request), pageable));
    }

    @Operation(summary = "Get booking by id", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponse> getBookingById(HttpServletRequest request, @PathVariable Long bookingId) {
        BookingResponse bookingResponse = bookingService.getById(bookingId);
        if (!bookingResponse.userId().equals(RequestHeaderUtils.getActiveUserId(request)) &&
                !RequestHeaderUtils.isAdmin(request)) {
            throw new AccessDeniedException("Only the user who made the booking can receive information about this booking");
        } else {
            return ResponseEntity.ok(bookingResponse);
        }
    }

    @Operation(summary = "Add new booking", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/add")
    public ResponseEntity<?> createBooking(HttpServletRequest request, @Valid @RequestBody CreateBookingRequest createBookingRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingService.create(
                        RequestHeaderUtils.getActiveUserId(request),
                        RequestHeaderUtils.getEmail(request),
                        createBookingRequest)
                );
    }
}
