package com.vehiclesphere.dto.request.booking;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelBookingRequest {

    @NotNull
    private UUID bookingId;
}
