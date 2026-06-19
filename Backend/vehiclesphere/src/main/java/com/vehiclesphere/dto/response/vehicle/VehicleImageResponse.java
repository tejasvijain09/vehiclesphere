package com.vehiclesphere.dto.response.vehicle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleImageResponse {

    private UUID id;
    private String imageUrl;
    private boolean primaryImage;
}