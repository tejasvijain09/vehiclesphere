package com.vehiclesphere.dashboard;

import com.vehiclesphere.booking.BookingRepository;
import com.vehiclesphere.booking.BookingService;
import com.vehiclesphere.booking.BookingStatus;
import com.vehiclesphere.booking.dto.BookingResponseDto;
import com.vehiclesphere.dashboard.dto.AdminDashboardStatsDto;
import com.vehiclesphere.dashboard.dto.CustomerDashboardStatsDto;
import com.vehiclesphere.user.Role;
import com.vehiclesphere.user.UserRepository;
import com.vehiclesphere.vehicle.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final BookingRepository bookingRepository;
    private final BookingService bookingService;

    @Transactional(readOnly = true)
    public AdminDashboardStatsDto getAdminStats() {
        long totalCustomers = userRepository.countByRole(Role.CUSTOMER);
        long totalVehicles = vehicleRepository.count();
        long availableVehicles = vehicleRepository.countByAvailable(true);
        long totalBookings = bookingRepository.count();
        long pendingBookings = bookingRepository.countByStatus(BookingStatus.PENDING);
        long activeRentals = bookingRepository.countByStatus(BookingStatus.ACTIVE);
        long completedBookings = bookingRepository.countByStatus(BookingStatus.COMPLETED);
        BigDecimal totalRevenue = bookingRepository.calculateTotalRevenue();

        List<BookingResponseDto> recentBookings = bookingRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .limit(5)
                .map(bookingService::mapToDto)
                .toList();

        return AdminDashboardStatsDto.builder()
                .totalCustomers(totalCustomers)
                .totalVehicles(totalVehicles)
                .availableVehicles(availableVehicles)
                .totalBookings(totalBookings)
                .pendingBookings(pendingBookings)
                .activeRentals(activeRentals)
                .completedBookings(completedBookings)
                .totalRevenue(totalRevenue != null ? totalRevenue : BigDecimal.ZERO)
                .recentBookings(recentBookings)
                .build();
    }

    @Transactional(readOnly = true)
    public CustomerDashboardStatsDto getCustomerStats(Long userId) {
        long totalBookings = bookingRepository.countByUserId(userId);
        long pendingBookings = bookingRepository.countByUserIdAndStatus(userId, BookingStatus.PENDING);
        long activeRentals = bookingRepository.countByUserIdAndStatus(userId, BookingStatus.ACTIVE);
        long completedBookings = bookingRepository.countByUserIdAndStatus(userId, BookingStatus.COMPLETED);
        long cancelledBookings = bookingRepository.countByUserIdAndStatus(userId, BookingStatus.CANCELLED);

        List<BookingResponseDto> recentBookings = bookingRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .limit(5)
                .map(bookingService::mapToDto)
                .toList();

        return CustomerDashboardStatsDto.builder()
                .totalBookings(totalBookings)
                .pendingBookings(pendingBookings)
                .activeRentals(activeRentals)
                .completedBookings(completedBookings)
                .cancelledBookings(cancelledBookings)
                .recentBookings(recentBookings)
                .build();
    }
}
