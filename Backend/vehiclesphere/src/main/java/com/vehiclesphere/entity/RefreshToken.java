package com.vehiclesphere.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Table(name = "refresh_tokens")
public class RefreshToken extends BaseEntity{

    @Column(unique = true)
    private String token;

    private LocalDateTime expiryDate;
    private boolean revoked;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
