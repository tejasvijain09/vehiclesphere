package com.vehiclesphere.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity{

    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;
    @Column(unique = true)

    private String phoneNumber;
    private boolean active;
    private boolean emailVerified;
    private String profileImageUrl;
}
