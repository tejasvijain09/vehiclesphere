package com.vehiclesphere.entity;

import com.vehiclesphere.enums.FuelType;
import com.vehiclesphere.enums.VehicleStatus;
import com.vehiclesphere.enums.VehicleType;
import jakarta.persistence.Entity;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle extends BaseEntity{

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
}
