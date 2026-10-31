package com.vehiclesphere.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderResponse {
    private String orderId;
    private Long amountInPaise;
    private BigDecimal amountInRupees;
    private String currency;
    private String keyId;
    private String bookingReference;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String vehicleName;
}
