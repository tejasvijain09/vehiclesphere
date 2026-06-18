package com.vehiclesphere.entity;

import com.vehiclesphere.enums.RoleType;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role extends BaseEntity{

    private RoleType name;
    private String description;
}
