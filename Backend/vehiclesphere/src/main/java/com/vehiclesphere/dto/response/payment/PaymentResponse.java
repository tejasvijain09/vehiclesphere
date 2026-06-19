package com.vehiclesphere.dto.response.payment;

import com.vehiclesphere.enums.PaymentMethod;
import com.vehiclesphere.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private UUID id;

    private BigDecimal amount;

    private String transactionId;

    private PaymentMethod paymentMethod;

    private PaymentStatus status;
}
