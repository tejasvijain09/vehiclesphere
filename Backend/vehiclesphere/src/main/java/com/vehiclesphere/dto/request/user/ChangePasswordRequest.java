package com.vehiclesphere.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {

    @NotBlank
    private String currentPassword;

    @Size(min = 6)
    private String newPassword;

    @NotBlank
    private String confirmPassword;
}