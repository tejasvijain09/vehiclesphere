package com.vehiclesphere.entity;

import com.vehiclesphere.enums.FuelType;
import com.vehiclesphere.enums.VehicleStatus;
import com.vehiclesphere.enums.VehicleType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "vehicle")
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "vehicle")
    private List<Review> reviews = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private VehicleCategory category;

    @OneToMany(mappedBy = "vehicle",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<VehicleImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "vehicle",cascade = CascadeType.ALL)
    private List<MaintenanceRecord> maintenanceRecords = new ArrayList<>();
}
