package com.vehiclesphere;

import com.vehiclesphere.category.Category;
import com.vehiclesphere.category.CategoryRepository;
import com.vehiclesphere.vehicle.Vehicle;
import com.vehiclesphere.vehicle.VehicleRepository;
import com.vehiclesphere.vehicle.VehicleService;
import com.vehiclesphere.vehicle.dto.VehicleDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private VehicleService vehicleService;

    private Vehicle sampleVehicle;
    private Category sampleCategory;

    @BeforeEach
    void setUp() {
        sampleCategory = Category.builder()
                .id(1L)
                .name("SUV")
                .description("Luxury SUVs for weddings")
                .build();

        sampleVehicle = Vehicle.builder()
                .id(5L)
                .name("Toyota Fortuner Legender")
                .brand("Toyota")
                .model("Fortuner Legender 4x4")
                .category(sampleCategory)
                .location("Amritsar / Ludhiana")
                .description("Iconic VIP white convoy SUV for royal baraat arrivals")
                .registrationNumber("PB-02-FL-8888")
                .fuelType("Diesel")
                .transmission("Automatic")
                .seatingCapacity(7)
                .modelYear(2023)
                .pricePerDay(new BigDecimal("7500.00"))
                .imageUrl("https://images.unsplash.com/photo-1533473359331-0135ef1b58bf")
                .available(true)
                .build();
    }

    @Test
    void testSearchVehicles_ByKeywordAndFilters() {
        when(vehicleRepository.findAll(any(Specification.class))).thenReturn(List.of(sampleVehicle));

        List<VehicleDto> results = vehicleService.searchVehicles("Fortuner", 1L, "Diesel", new BigDecimal("10000"), true);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Toyota Fortuner Legender", results.get(0).getName());
        assertEquals("Toyota", results.get(0).getBrand());
        assertEquals(new BigDecimal("7500.00"), results.get(0).getPricePerDay());
        verify(vehicleRepository, times(1)).findAll(any(Specification.class));
    }
}
