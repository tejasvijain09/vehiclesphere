package com.vehiclesphere.dto.request.vehicle;

import com.vehiclesphere.enums.VehicleStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVehicleRequest {

    private String color;
    private String city;
    private String location;
    private BigDecimal pricePerDay;
    private VehicleStatus status;
    private String description;
}
