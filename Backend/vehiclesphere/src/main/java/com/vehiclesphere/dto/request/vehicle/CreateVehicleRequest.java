package com.vehiclesphere.dto.request.vehicle;

import com.vehiclesphere.enums.FuelType;
import com.vehiclesphere.enums.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateVehicleRequest {

    @NotBlank
    private String registrationNumber;

    @NotBlank
    private String brand;

    @NotBlank
    private String model;

    @NotNull
    private Integer manufacturingYear;

    @NotBlank
    private String color;

    @NotNull
    private VehicleType vehicleType;

    @NotNull
    private FuelType fuelType;

    @NotNull
    private Integer seatingCapacity;

    @NotBlank
    private String city;

    @NotBlank
    private String location;

    @NotNull
    @Positive
    private BigDecimal pricePerDay;

    @NotBlank
    private String description;

    @NotNull
    private UUID categoryId;
}
