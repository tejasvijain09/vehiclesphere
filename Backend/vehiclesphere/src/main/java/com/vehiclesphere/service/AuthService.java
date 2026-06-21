package com.vehiclesphere.service;

import com.vehiclesphere.dto.request.auth.LoginRequest;
import com.vehiclesphere.dto.request.auth.RegisterRequest;
import com.vehiclesphere.dto.response.auth.TokenResponse;
import com.vehiclesphere.dto.response.user.UserResponse;

public interface AuthService {

    UserResponse register(RegisterRequest request);

    TokenResponse login(LoginRequest request);
}