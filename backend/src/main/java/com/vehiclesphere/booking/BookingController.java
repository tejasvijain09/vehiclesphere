package com.vehiclesphere.booking;

import com.vehiclesphere.booking.dto.BookingResponseDto;
import com.vehiclesphere.booking.dto.CreateBookingRequest;
import com.vehiclesphere.booking.dto.PriceCalculationResponse;
import com.vehiclesphere.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@Tag(name = "Booking Operations", description = "Endpoints for customer bookings and price calculation")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @Operation(summary = "Create a new booking request (Customer)")
    public ResponseEntity<BookingResponseDto> createBooking(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateBookingRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingService.createBooking(principal.getId(), request));
    }

    @GetMapping("/my")
    @Operation(summary = "Get bookings for the logged-in customer")
    public ResponseEntity<List<BookingResponseDto>> getMyBookings(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return ResponseEntity.ok(bookingService.getCustomerBookings(principal.getId()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get booking details by ID")
    public ResponseEntity<BookingResponseDto> getBookingById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return ResponseEntity.ok(bookingService.getBookingById(id, principal.getId(), principal.getRole()));
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancel an eligible booking")
    public ResponseEntity<BookingResponseDto> cancelBooking(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return ResponseEntity.ok(bookingService.cancelBooking(id, principal.getId(), principal.getRole()));
    }

    @GetMapping("/calculate-price")
    @Operation(summary = "Calculate rental price and check availability for dates (Public)")
    public ResponseEntity<PriceCalculationResponse> calculatePrice(
            @RequestParam Long vehicleId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate pickupDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate
    ) {
        return ResponseEntity.ok(bookingService.calculatePrice(vehicleId, pickupDate, returnDate));
    }
}
