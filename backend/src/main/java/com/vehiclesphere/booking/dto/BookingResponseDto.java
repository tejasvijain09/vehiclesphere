package com.vehiclesphere.booking.dto;

import com.vehiclesphere.booking.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDto {
    private Long id;
    private String bookingReference;
    private Long userId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private Long vehicleId;
    private String vehicleName;
    private String vehicleBrand;
    private String vehicleModel;
    private String vehicleImageUrl;
    private String vehicleRegistrationNumber;
    private LocalDate pickupDate;
    private LocalDate returnDate;
    private String pickupLocation;
    private Integer numberOfDays;
    private BigDecimal pricePerDay;
    private BigDecimal totalAmount;
    private BookingStatus status;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
