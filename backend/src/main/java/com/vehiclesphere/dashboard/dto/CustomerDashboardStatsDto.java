package com.vehiclesphere.dashboard.dto;

import com.vehiclesphere.booking.dto.BookingResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDashboardStatsDto {
    private long totalBookings;
    private long pendingBookings;
    private long activeRentals;
    private long completedBookings;
    private long cancelledBookings;
    private List<BookingResponseDto> recentBookings;
}
