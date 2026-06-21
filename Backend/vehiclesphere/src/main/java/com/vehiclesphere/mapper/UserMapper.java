package com.vehiclesphere.mapper;

import com.vehiclesphere.dto.request.auth.RegisterRequest;
import com.vehiclesphere.dto.response.user.UserResponse;
import com.vehiclesphere.entity.Role;
import com.vehiclesphere.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(RegisterRequest request, Role role) {

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhoneNumber(request.getPhoneNumber());

        user.setRole(role);

        user.setActive(true);
        user.setEmailVerified(false);

        return user;
    }

    public UserResponse toResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole().getName().name())
                .active(user.isActive())
                .build();
    }
}