package com.vehiclesphere.booking;

import com.vehiclesphere.booking.dto.BookingResponseDto;
import com.vehiclesphere.booking.dto.BookingStatusUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/bookings")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Booking Management", description = "Endpoints for administrator booking actions and status updates")
public class AdminBookingController {

    private final BookingService bookingService;

    @GetMapping
    @Operation(summary = "Get all customer bookings across the platform (Admin only)")
    public ResponseEntity<List<BookingResponseDto>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update booking status (Approve, Reject, Active, Completed) (Admin only)")
    public ResponseEntity<BookingResponseDto> updateBookingStatus(
            @PathVariable Long id,
            @Valid @RequestBody BookingStatusUpdateRequest request
    ) {
        return ResponseEntity.ok(bookingService.updateBookingStatus(id, request));
    }
}
