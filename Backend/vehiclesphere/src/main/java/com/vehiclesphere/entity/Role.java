package com.vehiclesphere.entity;

import com.vehiclesphere.enums.RoleType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role extends BaseEntity{

    private RoleType name;
    private String description;

    @OneToMany(mappedBy = "role")
    private List<User> users = new ArrayList<>();
}
