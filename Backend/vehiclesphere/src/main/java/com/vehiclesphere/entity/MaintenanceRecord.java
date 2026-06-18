package com.vehiclesphere.entity;

import jakarta.persistence.Entity;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceRecord extends BaseEntity{

    private String description;
    private LocalDate serviceDate;
    private LocalDate nextServiceDate;
    private BigDecimal cost;
    private boolean completed;
}
