package com.vehiclesphere.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);
    Optional<Payment> findByRazorpayPaymentId(String razorpayPaymentId);
    List<Payment> findByBookingIdOrderByCreatedAtDesc(Long bookingId);
    List<Payment> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Payment> findAllByOrderByCreatedAtDesc();
    boolean existsByBookingIdAndStatus(Long bookingId, PaymentStatus status);

    @org.springframework.data.jpa.repository.Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.status = com.vehiclesphere.payment.PaymentStatus.SUCCESS")
    java.math.BigDecimal calculateTotalPaidRevenue();
}
