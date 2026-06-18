package com.vehiclesphere.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken extends BaseEntity{

    @Column(unique = true)
    private String token;

    private LocalDateTime expiryDate;
    private boolean revoked;
}
