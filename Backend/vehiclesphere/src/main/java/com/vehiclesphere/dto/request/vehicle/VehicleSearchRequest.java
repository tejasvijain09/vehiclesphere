package com.vehiclesphere.dto.request.vehicle;

import com.vehiclesphere.enums.FuelType;
import com.vehiclesphere.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleSearchRequest {

    private String city;
    private VehicleType vehicleType;
    private FuelType fuelType;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
