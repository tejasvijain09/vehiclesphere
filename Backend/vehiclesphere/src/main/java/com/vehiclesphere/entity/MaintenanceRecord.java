package com.vehiclesphere.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Table(name = "maintenance_records")
public class MaintenanceRecord extends BaseEntity{

    private String description;
    private LocalDate serviceDate;
    private LocalDate nextServiceDate;
    private BigDecimal cost;
    private boolean completed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
}
