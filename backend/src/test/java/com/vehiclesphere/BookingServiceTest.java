package com.vehiclesphere;

import com.vehiclesphere.booking.Booking;
import com.vehiclesphere.booking.BookingRepository;
import com.vehiclesphere.booking.BookingService;
import com.vehiclesphere.booking.BookingStatus;
import com.vehiclesphere.booking.dto.BookingResponseDto;
import com.vehiclesphere.booking.dto.CreateBookingRequest;
import com.vehiclesphere.booking.dto.PriceCalculationResponse;
import com.vehiclesphere.category.Category;
import com.vehiclesphere.exception.BadRequestException;
import com.vehiclesphere.exception.ConflictException;
import com.vehiclesphere.user.Role;
import com.vehiclesphere.user.User;
import com.vehiclesphere.user.UserRepository;
import com.vehiclesphere.vehicle.Vehicle;
import com.vehiclesphere.vehicle.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private com.vehiclesphere.notification.EmailService emailService;

    @InjectMocks
    private BookingService bookingService;

    private User sampleUser;
    private Vehicle sampleVehicle;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder()
                .id(1L)
                .fullName("Gurpreet Singh")
                .email("gurpreet.singh@example.com")
                .phoneNumber("+91 98765 43210")
                .role(Role.CUSTOMER)
                .build();

        Category category = Category.builder().id(1L).name("Sedan").build();

        sampleVehicle = Vehicle.builder()
                .id(10L)
                .name("Tesla Model 3")
                .brand("Tesla")
                .model("Model 3")
                .category(category)
                .registrationNumber("TEST-100")
                .pricePerDay(new BigDecimal("100.00"))
                .available(true)
                .build();
    }

    @Test
    void testPriceCalculation_Success() {
        LocalDate pickup = LocalDate.now().plusDays(1);
        LocalDate returnDate = LocalDate.now().plusDays(4); // 3 days

        when(vehicleRepository.findById(10L)).thenReturn(Optional.of(sampleVehicle));
        when(bookingRepository.findConflictingBookings(10L, pickup, returnDate)).thenReturn(Collections.emptyList());

        PriceCalculationResponse response = bookingService.calculatePrice(10L, pickup, returnDate);

        assertNotNull(response);
        assertEquals(3, response.getNumberOfDays());
        assertEquals(new BigDecimal("100.00"), response.getPricePerDay());
        assertEquals(new BigDecimal("300.00"), response.getTotalAmount());
        assertTrue(response.getIsAvailable());
    }

    @Test
    void testCreateBooking_Success() {
        LocalDate pickup = LocalDate.now().plusDays(2);
        LocalDate returnDate = LocalDate.now().plusDays(5); // 3 days

        CreateBookingRequest request = CreateBookingRequest.builder()
                .vehicleId(10L)
                .pickupDate(pickup)
                .returnDate(returnDate)
                .pickupLocation("City Airport")
                .notes("Need GPS")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(vehicleRepository.findById(10L)).thenReturn(Optional.of(sampleVehicle));
        when(bookingRepository.findConflictingBookings(10L, pickup, returnDate)).thenReturn(Collections.emptyList());

        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking b = invocation.getArgument(0);
            b.setId(50L);
            return b;
        });

        BookingResponseDto response = bookingService.createBooking(1L, request);

        assertNotNull(response);
        assertEquals(BookingStatus.PENDING, response.getStatus());
        assertEquals(new BigDecimal("300.00"), response.getTotalAmount());
        assertEquals(3, response.getNumberOfDays());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void testCreateBooking_ConflictingDates_ThrowsConflictException() {
        LocalDate pickup = LocalDate.now().plusDays(2);
        LocalDate returnDate = LocalDate.now().plusDays(5);

        CreateBookingRequest request = CreateBookingRequest.builder()
                .vehicleId(10L)
                .pickupDate(pickup)
                .returnDate(returnDate)
                .pickupLocation("City Airport")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(vehicleRepository.findById(10L)).thenReturn(Optional.of(sampleVehicle));

        Booking existing = Booking.builder().id(99L).status(BookingStatus.CONFIRMED).build();
        when(bookingRepository.findConflictingBookings(10L, pickup, returnDate)).thenReturn(List.of(existing));

        assertThrows(ConflictException.class, () -> bookingService.createBooking(1L, request));
    }

    @Test
    void testCreateBooking_InvalidDates_ThrowsBadRequestException() {
        LocalDate pickup = LocalDate.now().plusDays(5);
        LocalDate returnDate = LocalDate.now().plusDays(2); // return before pickup

        CreateBookingRequest request = CreateBookingRequest.builder()
                .vehicleId(10L)
                .pickupDate(pickup)
                .returnDate(returnDate)
                .pickupLocation("City Airport")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(vehicleRepository.findById(10L)).thenReturn(Optional.of(sampleVehicle));

        assertThrows(BadRequestException.class, () -> bookingService.createBooking(1L, request));
    }
}
