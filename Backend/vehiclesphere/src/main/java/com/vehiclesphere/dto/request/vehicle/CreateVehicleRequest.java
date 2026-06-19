package com.vehiclesphere.dto.request.vehicle;

import com.vehiclesphere.enums.FuelType;
import com.vehiclesphere.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateVehicleRequest {

    private String registrationNumber;
    private String brand;
    private String model;
    private Integer manufacturingYear;
    private String color;

    private VehicleType vehicleType;
    private FuelType fuelType;

    private Integer seatingCapacity;

    private String city;
    private String location;

    private BigDecimal pricePerDay;

    private String description;

    private UUID categoryId;
}
