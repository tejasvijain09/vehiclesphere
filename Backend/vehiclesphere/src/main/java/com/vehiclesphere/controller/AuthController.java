package com.vehiclesphere.controller;

import com.vehiclesphere.dto.request.auth.RegisterRequest;
import com.vehiclesphere.dto.response.user.UserResponse;
import com.vehiclesphere.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public UserResponse register(
            @Valid
            @RequestBody
            RegisterRequest request
    ) {
        return authService.register(request);
    }
}