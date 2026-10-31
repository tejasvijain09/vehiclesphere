package com.vehiclesphere.dashboard;

import com.vehiclesphere.booking.Booking;
import com.vehiclesphere.booking.BookingRepository;
import com.vehiclesphere.booking.BookingService;
import com.vehiclesphere.booking.BookingStatus;
import com.vehiclesphere.booking.dto.BookingResponseDto;
import com.vehiclesphere.dashboard.dto.*;
import com.vehiclesphere.payment.Payment;
import com.vehiclesphere.payment.PaymentRepository;
import com.vehiclesphere.payment.PaymentStatus;
import com.vehiclesphere.user.Role;
import com.vehiclesphere.user.UserRepository;
import com.vehiclesphere.vehicle.Vehicle;
import com.vehiclesphere.vehicle.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final BookingRepository bookingRepository;
    private final BookingService bookingService;
    private final PaymentRepository paymentRepository;

    @Transactional(readOnly = true)
    public AdminDashboardStatsDto getAdminStats() {
        long totalCustomers = userRepository.countByRole(Role.CUSTOMER);
        long totalVehicles = vehicleRepository.count();
        long availableVehicles = vehicleRepository.countByAvailable(true);
        long totalBookings = bookingRepository.count();
        long pendingBookings = bookingRepository.countByStatus(BookingStatus.PENDING);
        long confirmedBookings = bookingRepository.countByStatus(BookingStatus.CONFIRMED);
        long activeRentals = bookingRepository.countByStatus(BookingStatus.ACTIVE);
        long completedBookings = bookingRepository.countByStatus(BookingStatus.COMPLETED);

        // Revenue strictly from successfully paid transactions
        BigDecimal totalRevenue = paymentRepository.calculateTotalPaidRevenue();
        if (totalRevenue == null) {
            totalRevenue = BigDecimal.ZERO;
        }

        List<Booking> allBookings = bookingRepository.findAll();
        List<Payment> successfulPayments = paymentRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .filter(p -> p.getStatus() == PaymentStatus.SUCCESS)
                .toList();

        // 1. Monthly Trends (Last 6 months)
        List<MonthlyDataDto> monthlyTrends = calculateMonthlyTrends(allBookings, successfulPayments);

        // 2. Booking Status Distribution
        List<StatusCountDto> statusDistribution = calculateStatusDistribution(allBookings);

        // 3. Top Rented Vehicles
        List<TopVehicleDto> topRentedVehicles = calculateTopRentedVehicles(allBookings, successfulPayments);

        List<BookingResponseDto> recentBookings = bookingRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .limit(6)
                .map(bookingService::mapToDto)
                .toList();

        return AdminDashboardStatsDto.builder()
                .totalCustomers(totalCustomers)
                .totalVehicles(totalVehicles)
                .availableVehicles(availableVehicles)
                .totalBookings(totalBookings)
                .pendingBookings(pendingBookings)
                .confirmedBookings(confirmedBookings)
                .activeRentals(activeRentals)
                .completedBookings(completedBookings)
                .totalRevenue(totalRevenue)
                .recentBookings(recentBookings)
                .monthlyTrends(monthlyTrends)
                .statusDistribution(statusDistribution)
                .topRentedVehicles(topRentedVehicles)
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

    private List<MonthlyDataDto> calculateMonthlyTrends(List<Booking> bookings, List<Payment> payments) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
        LocalDate now = LocalDate.now();

        List<MonthlyDataDto> result = new ArrayList<>();
        for (int i = 5; i >= 0; i--) {
            LocalDate monthDate = now.minusMonths(i);
            String monthKey = monthDate.format(formatter);
            int targetYear = monthDate.getYear();
            int targetMonth = monthDate.getMonthValue();

            long count = bookings.stream()
                    .filter(b -> b.getCreatedAt() != null &&
                            b.getCreatedAt().getYear() == targetYear &&
                            b.getCreatedAt().getMonthValue() == targetMonth)
                    .count();

            BigDecimal revenue = payments.stream()
                    .filter(p -> p.getCreatedAt() != null &&
                            p.getCreatedAt().getYear() == targetYear &&
                            p.getCreatedAt().getMonthValue() == targetMonth)
                    .map(Payment::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            result.add(MonthlyDataDto.builder()
                    .month(monthKey)
                    .bookings(count)
                    .revenue(revenue)
                    .build());
        }
        return result;
    }

    private List<StatusCountDto> calculateStatusDistribution(List<Booking> bookings) {
        Map<BookingStatus, Long> counts = bookings.stream()
                .collect(Collectors.groupingBy(Booking::getStatus, Collectors.counting()));

        List<StatusCountDto> distribution = new ArrayList<>();
        for (BookingStatus status : BookingStatus.values()) {
            distribution.add(StatusCountDto.builder()
                    .status(status.name())
                    .count(counts.getOrDefault(status, 0L))
                    .build());
        }
        return distribution;
    }

    private List<TopVehicleDto> calculateTopRentedVehicles(List<Booking> bookings, List<Payment> payments) {
        Map<Long, Long> rentalsPerVehicle = bookings.stream()
                .collect(Collectors.groupingBy(b -> b.getVehicle().getId(), Collectors.counting()));

        Map<Long, BigDecimal> revenuePerVehicle = new HashMap<>();
        for (Payment p : payments) {
            Long vId = p.getBooking().getVehicle().getId();
            revenuePerVehicle.merge(vId, p.getAmount(), BigDecimal::add);
        }

        List<Vehicle> allVehicles = vehicleRepository.findAll();

        return allVehicles.stream()
                .map(v -> TopVehicleDto.builder()
                        .vehicleId(v.getId())
                        .vehicleName(v.getName())
                        .category(v.getCategory().getName())
                        .rentalCount(rentalsPerVehicle.getOrDefault(v.getId(), 0L))
                        .revenueGenerated(revenuePerVehicle.getOrDefault(v.getId(), BigDecimal.ZERO))
                        .build())
                .sorted(Comparator.comparingLong(TopVehicleDto::getRentalCount).reversed()
                        .thenComparing(TopVehicleDto::getRevenueGenerated, Comparator.reverseOrder()))
                .limit(5)
                .toList();
    }
}
