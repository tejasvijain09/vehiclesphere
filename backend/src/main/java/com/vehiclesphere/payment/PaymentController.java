package com.vehiclesphere.payment;

import com.vehiclesphere.payment.dto.CreateOrderRequest;
import com.vehiclesphere.payment.dto.CreateOrderResponse;
import com.vehiclesphere.payment.dto.PaymentResponseDto;
import com.vehiclesphere.payment.dto.VerifyPaymentRequest;
import com.vehiclesphere.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Razorpay payment processing and transaction verification")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create-order")
    @Operation(summary = "Create Razorpay payment order for a booking")
    public ResponseEntity<CreateOrderResponse> createOrder(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateOrderRequest request
    ) {
        return ResponseEntity.ok(paymentService.createPaymentOrder(request.getBookingId(), principal.getId()));
    }

    @PostMapping("/verify")
    @Operation(summary = "Verify Razorpay payment signature and confirm transaction")
    public ResponseEntity<PaymentResponseDto> verifyPayment(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody VerifyPaymentRequest request
    ) {
        return ResponseEntity.ok(paymentService.verifyPayment(request, principal.getId()));
    }

    @GetMapping("/my-payments")
    @Operation(summary = "Get current customer payment transaction history")
    public ResponseEntity<List<PaymentResponseDto>> getMyPayments(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return ResponseEntity.ok(paymentService.getMyPayments(principal.getId()));
    }

    @GetMapping("/booking/{bookingId}")
    @Operation(summary = "Get payment records for a specific booking")
    public ResponseEntity<List<PaymentResponseDto>> getBookingPayments(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long bookingId
    ) {
        return ResponseEntity.ok(paymentService.getBookingPayments(bookingId, principal.getId(), principal.getRole()));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all payment transactions across the platform (Admin only)")
    public ResponseEntity<List<PaymentResponseDto>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }
}
