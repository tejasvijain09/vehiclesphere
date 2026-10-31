package com.vehiclesphere.review;

import com.vehiclesphere.review.dto.CreateReviewRequest;
import com.vehiclesphere.review.dto.ReviewResponseDto;
import com.vehiclesphere.review.dto.VehicleRatingSummaryDto;
import com.vehiclesphere.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Customer reviews and vehicle rating management")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @Operation(summary = "Submit a rating and review for a completed vehicle rental")
    public ResponseEntity<ReviewResponseDto> createReview(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateReviewRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(principal.getId(), request));
    }

    @GetMapping("/vehicle/{vehicleId}")
    @Operation(summary = "Get all approved reviews for a specific vehicle (Public)")
    public ResponseEntity<List<ReviewResponseDto>> getVehicleReviews(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(reviewService.getVehicleReviews(vehicleId));
    }

    @GetMapping("/vehicle/{vehicleId}/summary")
    @Operation(summary = "Get average star rating and review count for a vehicle (Public)")
    public ResponseEntity<VehicleRatingSummaryDto> getVehicleRatingSummary(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(reviewService.getVehicleRatingSummary(vehicleId));
    }

    @GetMapping("/my-reviews")
    @Operation(summary = "Get all reviews submitted by the current logged-in customer")
    public ResponseEntity<List<ReviewResponseDto>> getMyReviews(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(reviewService.getMyReviews(principal.getId()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a review (Author or Admin only)")
    public ResponseEntity<Void> deleteReview(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id
    ) {
        reviewService.deleteReview(id, principal.getId(), principal.getRole());
        return ResponseEntity.noContent().build();
    }
}
