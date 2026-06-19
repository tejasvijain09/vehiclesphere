package com.vehiclesphere.dto.request.payment;

import com.vehiclesphere.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentRequest {

    private UUID bookingId;

    private PaymentMethod paymentMethod;
}
