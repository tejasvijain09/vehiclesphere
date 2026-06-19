package com.vehiclesphere.dto.response.vehicle;

import com.vehiclesphere.enums.FuelType;
import com.vehiclesphere.enums.VehicleStatus;
import com.vehiclesphere.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleResponse {

    private UUID id;

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
    private Double averageRating;
    private VehicleStatus status;
    private String description;
    private VehicleCategoryResponse category;
    private List<VehicleImageResponse> images;
}
