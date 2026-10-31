package com.vehiclesphere.user;

import com.vehiclesphere.security.UserPrincipal;
import com.vehiclesphere.user.dto.UpdateProfileRequest;
import com.vehiclesphere.user.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Endpoints for user profile and customer directory")
public class UserController {

    private final UserService userService;

    @GetMapping("/users/profile")
    @Operation(summary = "Get current logged-in user profile")
    public ResponseEntity<UserDto> getProfile(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(userService.getUserProfile(principal.getId()));
    }

    @PutMapping("/users/profile")
    @Operation(summary = "Update current user profile")
    public ResponseEntity<UserDto> updateProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        return ResponseEntity.ok(userService.updateProfile(principal.getId(), request));
    }

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all customer accounts (Admin only)")
    public ResponseEntity<List<UserDto>> getAllCustomers() {
        return ResponseEntity.ok(userService.getAllCustomers());
    }
}
