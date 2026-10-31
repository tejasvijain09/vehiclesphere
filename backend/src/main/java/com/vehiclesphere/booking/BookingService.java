package com.vehiclesphere.booking;

import com.vehiclesphere.booking.dto.BookingResponseDto;
import com.vehiclesphere.booking.dto.BookingStatusUpdateRequest;
import com.vehiclesphere.booking.dto.CreateBookingRequest;
import com.vehiclesphere.booking.dto.PriceCalculationResponse;
import com.vehiclesphere.exception.BadRequestException;
import com.vehiclesphere.exception.ConflictException;
import com.vehiclesphere.exception.ResourceNotFoundException;
import com.vehiclesphere.exception.UnauthorizedException;
import com.vehiclesphere.user.Role;
import com.vehiclesphere.user.User;
import com.vehiclesphere.user.UserRepository;
import com.vehiclesphere.vehicle.Vehicle;
import com.vehiclesphere.vehicle.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final com.vehiclesphere.notification.EmailService emailService;

    @Transactional
    public BookingResponseDto createBooking(Long userId, CreateBookingRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + request.getVehicleId()));

        if (!Boolean.TRUE.equals(vehicle.getAvailable())) {
            throw new BadRequestException("Vehicle is currently marked as unavailable for rent");
        }

        validateDates(request.getPickupDate(), request.getReturnDate());

        // Check for conflicting confirmed or active bookings
        List<Booking> conflicts = bookingRepository.findConflictingBookings(
                vehicle.getId(),
                request.getPickupDate(),
                request.getReturnDate()
        );
        if (!conflicts.isEmpty()) {
            throw new ConflictException("Vehicle is already booked for the selected date range");
        }

        int days = (int) ChronoUnit.DAYS.between(request.getPickupDate(), request.getReturnDate());
        BigDecimal totalAmount = vehicle.getPricePerDay().multiply(BigDecimal.valueOf(days));

        String reference = generateBookingReference();

        Booking booking = Booking.builder()
                .bookingReference(reference)
                .user(user)
                .vehicle(vehicle)
                .pickupDate(request.getPickupDate())
                .returnDate(request.getReturnDate())
                .pickupLocation(request.getPickupLocation())
                .numberOfDays(days)
                .pricePerDay(vehicle.getPricePerDay())
                .totalAmount(totalAmount)
                .status(BookingStatus.PENDING)
                .notes(request.getNotes())
                .build();

        Booking saved = bookingRepository.save(booking);
        emailService.sendBookingCreatedEmail(
                saved.getUser().getEmail(),
                saved.getUser().getFullName(),
                saved.getBookingReference(),
                saved.getVehicle().getName(),
                saved.getTotalAmount()
        );
        return mapToDto(saved);
    }

    @Transactional(readOnly = true)
    public PriceCalculationResponse calculatePrice(Long vehicleId, LocalDate pickupDate, LocalDate returnDate) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + vehicleId));

        validateDates(pickupDate, returnDate);

        List<Booking> conflicts = bookingRepository.findConflictingBookings(vehicleId, pickupDate, returnDate);
        boolean isAvailable = conflicts.isEmpty() && Boolean.TRUE.equals(vehicle.getAvailable());

        int days = (int) ChronoUnit.DAYS.between(pickupDate, returnDate);
        BigDecimal totalAmount = vehicle.getPricePerDay().multiply(BigDecimal.valueOf(days));

        return PriceCalculationResponse.builder()
                .vehicleId(vehicle.getId())
                .vehicleName(vehicle.getName())
                .pickupDate(pickupDate)
                .returnDate(returnDate)
                .numberOfDays(days)
                .pricePerDay(vehicle.getPricePerDay())
                .totalAmount(totalAmount)
                .isAvailable(isAvailable)
                .build();
    }

    @Transactional(readOnly = true)
    public List<BookingResponseDto> getCustomerBookings(Long userId) {
        return bookingRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BookingResponseDto> getAllBookings() {
        return bookingRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public BookingResponseDto getBookingById(Long bookingId, Long userId, Role userRole) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        if (userRole != Role.ADMIN && !booking.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to view this booking");
        }

        return mapToDto(booking);
    }

    @Transactional
    public BookingResponseDto cancelBooking(Long bookingId, Long userId, Role userRole) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        if (userRole != Role.ADMIN && !booking.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to cancel this booking");
        }

        if (booking.getStatus() == BookingStatus.COMPLETED || booking.getStatus() == BookingStatus.CANCELLED || booking.getStatus() == BookingStatus.REJECTED) {
            throw new BadRequestException("Booking cannot be cancelled because it is already " + booking.getStatus());
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setNotes((booking.getNotes() != null ? booking.getNotes() + " | " : "") + "Cancelled by " + userRole.name().toLowerCase());

        Booking saved = bookingRepository.save(booking);
        emailService.sendBookingStatusEmail(
                saved.getUser().getEmail(),
                saved.getUser().getFullName(),
                saved.getBookingReference(),
                saved.getVehicle().getName(),
                "CANCELLED",
                saved.getNotes()
        );
        return mapToDto(saved);
    }

    @Transactional
    public BookingResponseDto updateBookingStatus(Long bookingId, BookingStatusUpdateRequest request) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        // If transitioning to CONFIRMED or ACTIVE, check for conflicting bookings
        if (request.getStatus() == BookingStatus.CONFIRMED || request.getStatus() == BookingStatus.ACTIVE) {
            List<Booking> conflicts = bookingRepository.findConflictingBookings(
                    booking.getVehicle().getId(),
                    booking.getPickupDate(),
                    booking.getReturnDate()
            );
            boolean hasOtherConflict = conflicts.stream().anyMatch(b -> !b.getId().equals(booking.getId()));
            if (hasOtherConflict) {
                throw new ConflictException("Cannot approve booking: Vehicle is already booked by another confirmed rental in this date range");
            }
        }

        booking.setStatus(request.getStatus());
        if (request.getNotes() != null && !request.getNotes().isBlank()) {
            booking.setNotes(request.getNotes());
        }

        Booking saved = bookingRepository.save(booking);
        emailService.sendBookingStatusEmail(
                saved.getUser().getEmail(),
                saved.getUser().getFullName(),
                saved.getBookingReference(),
                saved.getVehicle().getName(),
                saved.getStatus().name(),
                saved.getNotes()
        );
        return mapToDto(saved);
    }

    private void validateDates(LocalDate pickupDate, LocalDate returnDate) {
        if (pickupDate == null || returnDate == null) {
            throw new BadRequestException("Both pickup date and return date must be provided");
        }
        if (pickupDate.isBefore(LocalDate.now())) {
            throw new BadRequestException("Pickup date cannot be in the past");
        }
        if (!returnDate.isAfter(pickupDate)) {
            throw new BadRequestException("Return date must be strictly after pickup date (minimum 1 day rental)");
        }
    }

    private String generateBookingReference() {
        int randomNum = 1000 + new Random().nextInt(9000);
        return "VSB-" + System.currentTimeMillis() % 1000000 + "-" + randomNum;
    }

    public BookingResponseDto mapToDto(Booking booking) {
        return BookingResponseDto.builder()
                .id(booking.getId())
                .bookingReference(booking.getBookingReference())
                .userId(booking.getUser().getId())
                .customerName(booking.getUser().getFullName())
                .customerEmail(booking.getUser().getEmail())
                .customerPhone(booking.getUser().getPhoneNumber())
                .vehicleId(booking.getVehicle().getId())
                .vehicleName(booking.getVehicle().getName())
                .vehicleBrand(booking.getVehicle().getBrand())
                .vehicleModel(booking.getVehicle().getModel())
                .vehicleImageUrl(booking.getVehicle().getImageUrl())
                .vehicleRegistrationNumber(booking.getVehicle().getRegistrationNumber())
                .pickupDate(booking.getPickupDate())
                .returnDate(booking.getReturnDate())
                .pickupLocation(booking.getPickupLocation())
                .numberOfDays(booking.getNumberOfDays())
                .pricePerDay(booking.getPricePerDay())
                .totalAmount(booking.getTotalAmount())
                .status(booking.getStatus())
                .paymentStatus(booking.getPaymentStatus() != null ? booking.getPaymentStatus() : "UNPAID")
                .notes(booking.getNotes())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }
}
