package com.vehiclesphere.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {
    private Long id;
    private Long vehicleId;
    private String vehicleName;
    private Long userId;
    private String customerName;
    private Long bookingId;
    private String bookingReference;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
