package com.vehiclesphere.service;

import com.vehiclesphere.dto.request.user.ChangePasswordRequest;
import com.vehiclesphere.dto.request.user.UpdateUserRequest;
import com.vehiclesphere.dto.response.user.UserResponse;

import java.util.UUID;

public interface UserService {

    UserResponse getUserById(UUID userId);

    UserResponse updateUser(
            UUID userId,
            UpdateUserRequest request
    );

    void changePassword(
            UUID userId,
            ChangePasswordRequest request
    );
}