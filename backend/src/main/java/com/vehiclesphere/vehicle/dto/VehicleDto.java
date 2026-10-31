package com.vehiclesphere.vehicle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDto {
    private Long id;
    private String name;
    private String brand;
    private String model;
    private Long categoryId;
    private String categoryName;
    private String registrationNumber;
    private String description;
    private String fuelType;
    private String transmission;
    private Integer seatingCapacity;
    private Integer modelYear;
    private BigDecimal pricePerDay;
    private String location;
    private String imageUrl;
    private Boolean available;
    private LocalDateTime createdAt;
}
