package com.vehiclesphere.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Phone number must be 10 digits"
    )
    private String phoneNumber;

    private String profileImageUrl;
}