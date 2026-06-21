package com.vehiclesphere.dto.request.booking;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    private String pickupLocation;

    @NotBlank
    private String dropLocation;
}