package com.vehiclesphere.entity;

import com.vehiclesphere.enums.PaymentMethod;
import com.vehiclesphere.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Table(name = "payments")
public class Payment extends BaseEntity{

    private BigDecimal amount;
    private String transactionId;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private LocalDateTime paidAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;
}
