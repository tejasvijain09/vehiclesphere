package com.vehiclesphere.dto.request.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookingRequest {

    private LocalDate startDate;
    private LocalDate endDate;

    private String pickupLocation;
    private String dropLocation;
}