package com.vehiclesphere.dto.request.review;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewRequest {

    @NotNull
    private UUID vehicleId;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;

    @NotBlank
    @Size(max = 1000)
    private String comment;
}
