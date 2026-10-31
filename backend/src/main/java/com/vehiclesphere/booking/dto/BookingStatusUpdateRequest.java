package com.vehiclesphere.booking.dto;

import com.vehiclesphere.booking.BookingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingStatusUpdateRequest {

    @NotNull(message = "Booking status is required")
    private BookingStatus status;

    private String notes;
}
