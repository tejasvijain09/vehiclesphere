package com.vehiclesphere.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleCategory extends BaseEntity{

    private String name;
    private String description;
}
