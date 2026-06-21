package com.vehiclesphere.entity;

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

@Table(name = "users")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Notification> notifications = new ArrayList<>();

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private RefreshToken refreshToken;
}
