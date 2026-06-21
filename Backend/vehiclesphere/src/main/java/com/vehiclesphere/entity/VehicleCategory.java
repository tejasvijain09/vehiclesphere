package com.vehiclesphere.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Table(name = "vehicle_categories")
public class VehicleCategory extends BaseEntity{

    private String name;
    private String description;

    @OneToMany(mappedBy = "category")
    private List<Vehicle> vehicles = new ArrayList<>();
}
