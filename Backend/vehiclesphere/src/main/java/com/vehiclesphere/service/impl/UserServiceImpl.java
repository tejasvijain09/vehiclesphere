package com.vehiclesphere.service.impl;

import com.vehiclesphere.dto.request.user.ChangePasswordRequest;
import com.vehiclesphere.dto.request.user.UpdateUserRequest;
import com.vehiclesphere.dto.response.user.UserResponse;
import com.vehiclesphere.mapper.UserMapper;
import com.vehiclesphere.repository.UserRepository;
import com.vehiclesphere.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl
        implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public UserResponse getUserById(
            UUID userId
    ) {
        return null;
    }

    @Override
    public UserResponse updateUser(
            UUID userId,
            UpdateUserRequest request
    ) {
        return null;
    }

    @Override
    public void changePassword(
            UUID userId,
            ChangePasswordRequest request
    ) {

    }
}