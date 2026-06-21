package com.vehiclesphere.dto.request.vehicle;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadVehicleImageRequest {

    @NotNull
    private MultipartFile image;

    private boolean primaryImage;
}