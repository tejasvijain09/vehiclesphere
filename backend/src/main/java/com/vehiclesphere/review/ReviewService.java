package com.vehiclesphere.review;

import com.vehiclesphere.booking.Booking;
import com.vehiclesphere.booking.BookingRepository;
import com.vehiclesphere.booking.BookingStatus;
import com.vehiclesphere.exception.BadRequestException;
import com.vehiclesphere.exception.ConflictException;
import com.vehiclesphere.exception.ResourceNotFoundException;
import com.vehiclesphere.exception.UnauthorizedException;
import com.vehiclesphere.review.dto.CreateReviewRequest;
import com.vehiclesphere.review.dto.ReviewResponseDto;
import com.vehiclesphere.review.dto.VehicleRatingSummaryDto;
import com.vehiclesphere.user.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    public ReviewResponseDto createReview(Long userId, CreateReviewRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + request.getBookingId()));

        if (!booking.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to review a booking that does not belong to you");
        }

        // Business rule: Only completed bookings can be reviewed
        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new BadRequestException("Only completed rentals can be reviewed. Current status: " + booking.getStatus());
        }

        // Business rule: Prevent duplicate reviews for the same booking
        if (reviewRepository.existsByBookingId(booking.getId())) {
            throw new ConflictException("You have already submitted a review for this booking");
        }

        Review review = Review.builder()
                .booking(booking)
                .vehicle(booking.getVehicle())
                .user(booking.getUser())
                .rating(request.getRating())
                .comment(request.getComment().trim())
                .build();

        review = reviewRepository.save(review);
        log.info("Review created for vehicle {} by customer {}", booking.getVehicle().getName(), booking.getUser().getFullName());

        return mapToDto(review);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getVehicleReviews(Long vehicleId) {
        return reviewRepository.findByVehicleIdOrderByCreatedAtDesc(vehicleId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public VehicleRatingSummaryDto getVehicleRatingSummary(Long vehicleId) {
        Double avg = reviewRepository.getAverageRatingByVehicleId(vehicleId);
        long count = reviewRepository.countByVehicleId(vehicleId);

        double roundedAvg = 0.0;
        if (avg != null) {
            roundedAvg = BigDecimal.valueOf(avg)
                    .setScale(1, RoundingMode.HALF_UP)
                    .doubleValue();
        }

        return VehicleRatingSummaryDto.builder()
                .vehicleId(vehicleId)
                .averageRating(roundedAvg)
                .totalReviews(count)
                .build();
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getMyReviews(Long userId) {
        return reviewRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Transactional
    public void deleteReview(Long reviewId, Long userId, Role userRole) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + reviewId));

        if (userRole != Role.ADMIN && !review.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to delete this review");
        }

        reviewRepository.delete(review);
        log.info("Review id {} deleted by user id {} with role {}", reviewId, userId, userRole);
    }

    private ReviewResponseDto mapToDto(Review review) {
        return ReviewResponseDto.builder()
                .id(review.getId())
                .vehicleId(review.getVehicle().getId())
                .vehicleName(review.getVehicle().getName())
                .userId(review.getUser().getId())
                .customerName(review.getUser().getFullName())
                .bookingId(review.getBooking().getId())
                .bookingReference(review.getBooking().getBookingReference())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
