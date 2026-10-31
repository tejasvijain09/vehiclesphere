package com.vehiclesphere.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceCalculationResponse {
    private Long vehicleId;
    private String vehicleName;
    private LocalDate pickupDate;
    private LocalDate returnDate;
    private Integer numberOfDays;
    private BigDecimal pricePerDay;
    private BigDecimal totalAmount;
    private Boolean isAvailable;
}
