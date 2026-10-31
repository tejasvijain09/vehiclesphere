package com.vehiclesphere.vehicle.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateVehicleRequest {

    @NotBlank(message = "Vehicle name is required")
    private String name;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotBlank(message = "Model is required")
    private String model;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotBlank(message = "Registration number is required")
    private String registrationNumber;

    private String description;

    @NotBlank(message = "Fuel type is required")
    private String fuelType;

    @NotBlank(message = "Transmission is required")
    private String transmission;

    @NotNull(message = "Seating capacity is required")
    @Min(value = 1, message = "Seating capacity must be at least 1")
    @Max(value = 20, message = "Seating capacity cannot exceed 20")
    private Integer seatingCapacity;

    @NotNull(message = "Model year is required")
    @Min(value = 1990, message = "Model year must be after 1990")
    private Integer modelYear;

    @NotNull(message = "Price per day is required")
    @DecimalMin(value = "0.01", message = "Price per day must be positive")
    private BigDecimal pricePerDay;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Image URL is required")
    private String imageUrl;

    @Builder.Default
    private Boolean available = true;
}
