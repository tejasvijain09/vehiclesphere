package com.vehiclesphere.entity;

import com.vehiclesphere.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Table(name = "bookings")
public class Booking extends BaseEntity{

    private LocalDate startDate;
    private LocalDate endDate;
    private String pickupLocation;
    private String dropLocation;
    private Integer totalDays;
    private BigDecimal totalAmount;
    private BookingStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @OneToOne(mappedBy = "booking",cascade = CascadeType.ALL)
    private Payment payment;
}
