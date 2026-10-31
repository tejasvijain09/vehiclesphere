package com.vehiclesphere.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopVehicleDto {
    private Long vehicleId;
    private String vehicleName;
    private String category;
    private long rentalCount;
    private BigDecimal revenueGenerated;
}
