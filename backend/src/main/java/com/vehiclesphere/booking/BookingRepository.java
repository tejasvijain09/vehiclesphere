package com.vehiclesphere.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByBookingReference(String bookingReference);

    List<Booking> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Booking> findAllByOrderByCreatedAtDesc();

    @Query("SELECT b FROM Booking b WHERE b.vehicle.id = :vehicleId " +
           "AND b.status IN (com.vehiclesphere.booking.BookingStatus.CONFIRMED, com.vehiclesphere.booking.BookingStatus.ACTIVE) " +
           "AND b.pickupDate <= :returnDate AND b.returnDate >= :pickupDate")
    List<Booking> findConflictingBookings(
            @Param("vehicleId") Long vehicleId,
            @Param("pickupDate") LocalDate pickupDate,
            @Param("returnDate") LocalDate returnDate
    );

    long countByStatus(BookingStatus status);

    long countByUserId(Long userId);

    long countByUserIdAndStatus(Long userId, BookingStatus status);

    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM Booking b WHERE b.status IN (com.vehiclesphere.booking.BookingStatus.CONFIRMED, com.vehiclesphere.booking.BookingStatus.ACTIVE, com.vehiclesphere.booking.BookingStatus.COMPLETED)")
    BigDecimal calculateTotalRevenue();
}
