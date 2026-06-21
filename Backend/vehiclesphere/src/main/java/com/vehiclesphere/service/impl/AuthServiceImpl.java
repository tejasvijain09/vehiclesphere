package com.vehiclesphere.service.impl;

import com.vehiclesphere.dto.request.auth.LoginRequest;
import com.vehiclesphere.dto.request.auth.RegisterRequest;
import com.vehiclesphere.dto.response.auth.TokenResponse;
import com.vehiclesphere.dto.response.user.UserResponse;
import com.vehiclesphere.entity.Role;
import com.vehiclesphere.entity.User;
import com.vehiclesphere.enums.RoleType;
import com.vehiclesphere.exception.DuplicateResourceException;
import com.vehiclesphere.exception.ResourceNotFoundException;
import com.vehiclesphere.mapper.UserMapper;
import com.vehiclesphere.repository.RoleRepository;
import com.vehiclesphere.repository.UserRepository;
import com.vehiclesphere.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl
        implements AuthService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    @Override
    public UserResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "Email already exists"
            );
        }

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new DuplicateResourceException(
                    "Phone number already exists"
            );
        }

        Role role = roleRepository
                        .findByName(RoleType.CUSTOMER)
                        .orElseThrow(() -> new ResourceNotFoundException("Customer role not found"));

        User user = userMapper.toEntity(
                        request,
                        role
                );

        user = userRepository.save(user);

        return userMapper.toResponse(user);
    }

    @Override
    public TokenResponse login(LoginRequest request) {
        return null;
    }
}