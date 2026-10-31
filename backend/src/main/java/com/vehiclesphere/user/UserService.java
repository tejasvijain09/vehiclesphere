package com.vehiclesphere.user;

import com.vehiclesphere.exception.ResourceNotFoundException;
import com.vehiclesphere.user.dto.UpdateProfileRequest;
import com.vehiclesphere.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return mapToDto(user);
    }

    @Transactional
    public UserDto updateProfile(Long userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        return mapToDto(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllCustomers() {
        return userRepository.findByRole(Role.CUSTOMER).stream()
                .map(this::mapToDto)
                .toList();
    }

    public UserDto mapToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
