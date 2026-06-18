package com.vehiclesphere.entity;

import com.vehiclesphere.enums.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification extends BaseEntity{

    private String title;

    @Column(length = 1000)
    private String message;

    private NotificationType type;
    private boolean read;
}
