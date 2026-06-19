package com.vehiclesphere.dto.request.vehicle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadVehicleImageRequest {

    private MultipartFile image;
    private boolean primaryImage;
}