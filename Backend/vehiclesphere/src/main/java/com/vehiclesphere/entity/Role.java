package com.vehiclesphere.entity;

import com.vehiclesphere.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Table(name = "roles")
public class Role extends BaseEntity{

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType name;

    private String description;

    @OneToMany(mappedBy = "role")
    private List<User> users = new ArrayList<>();
}
