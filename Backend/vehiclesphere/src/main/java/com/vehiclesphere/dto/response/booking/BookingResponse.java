package com.vehiclesphere.dto.response.booking;

import com.vehiclesphere.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {

    private UUID id;

    private UUID vehicleId;

    private LocalDate startDate;
    private LocalDate endDate;

    private Integer totalDays;
    private BigDecimal totalAmount;

    private BookingStatus status;
}
