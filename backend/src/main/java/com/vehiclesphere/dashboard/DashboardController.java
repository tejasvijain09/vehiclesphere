package com.vehiclesphere.dashboard;

import com.vehiclesphere.dashboard.dto.AdminDashboardStatsDto;
import com.vehiclesphere.dashboard.dto.CustomerDashboardStatsDto;
import com.vehiclesphere.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Endpoints for admin and customer overview statistics")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping({"/admin/dashboard", "/dashboard/stats"})
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get admin dashboard statistics (Admin only)")
    public ResponseEntity<AdminDashboardStatsDto> getAdminDashboard() {
        return ResponseEntity.ok(dashboardService.getAdminStats());
    }

    @GetMapping("/customer/dashboard")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @Operation(summary = "Get customer dashboard statistics")
    public ResponseEntity<CustomerDashboardStatsDto> getCustomerDashboard(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return ResponseEntity.ok(dashboardService.getCustomerStats(principal.getId()));
    }
}
