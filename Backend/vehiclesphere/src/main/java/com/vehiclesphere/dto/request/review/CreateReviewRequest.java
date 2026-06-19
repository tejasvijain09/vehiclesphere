package com.vehiclesphere.dto.request.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewRequest {

    private UUID vehicleId;

    private Integer rating;

    private String comment;
}
