package com.vehiclesphere.dto.response.notification;

import com.vehiclesphere.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

    private UUID id;

    private String title;

    private String message;

    private NotificationType type;

    private boolean read;

    private LocalDateTime createdAt;
}
