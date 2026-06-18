package com.vehiclesphere.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleImage extends BaseEntity{

    private String imageUrl;
    private boolean primaryImage;
}
