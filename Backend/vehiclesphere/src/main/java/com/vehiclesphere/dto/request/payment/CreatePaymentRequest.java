package com.vehiclesphere.dto.request.payment;

import com.vehiclesphere.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentRequest {

    @NotNull
    private UUID bookingId;

    @NotNull
    private PaymentMethod paymentMethod;
}
