package com.vehiclesphere.dashboard.dto;

import com.vehiclesphere.booking.dto.BookingResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardStatsDto {
    private long totalCustomers;
    private long totalVehicles;
    private long availableVehicles;
    private long totalBookings;
    private long pendingBookings;
    private long activeRentals;
    private long completedBookings;
    private BigDecimal totalRevenue;
    private List<BookingResponseDto> recentBookings;
}
