package com.vehiclesphere.entity;

import com.vehiclesphere.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Table(name = "notifications")
public class Notification extends BaseEntity{

    private String title;

    @Column(length = 1000)
    private String message;

    private NotificationType type;
    private boolean read;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
