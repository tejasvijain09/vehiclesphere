package com.vehiclesphere.dto.request.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingRequest {

    private UUID vehicleId;

    private LocalDate startDate;
    private LocalDate endDate;

    private String pickupLocation;
    private String dropLocation;
}
