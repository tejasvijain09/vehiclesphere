package com.vehiclesphere.vehicle;

import com.vehiclesphere.vehicle.dto.CreateVehicleRequest;
import com.vehiclesphere.vehicle.dto.UpdateVehicleRequest;
import com.vehiclesphere.vehicle.dto.VehicleDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
@Tag(name = "Vehicle Management", description = "Endpoints for vehicle browsing and administrative operations")
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    @Operation(summary = "Search and filter vehicles (Public)")
    public ResponseEntity<List<VehicleDto>> getVehicles(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String fuelType,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false, defaultValue = "false") Boolean availableOnly
    ) {
        return ResponseEntity.ok(vehicleService.searchVehicles(search, categoryId, fuelType, maxPrice, availableOnly));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get vehicle details by ID (Public)")
    public ResponseEntity<VehicleDto> getVehicleById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getVehicleById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new vehicle (Admin only)")
    public ResponseEntity<VehicleDto> createVehicle(@Valid @RequestBody CreateVehicleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleService.createVehicle(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update an existing vehicle (Admin only)")
    public ResponseEntity<VehicleDto> updateVehicle(
            @PathVariable Long id,
            @Valid @RequestBody UpdateVehicleRequest request
    ) {
        return ResponseEntity.ok(vehicleService.updateVehicle(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete vehicle by ID (Admin only)")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle-availability")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Toggle vehicle availability status (Admin only)")
    public ResponseEntity<VehicleDto> toggleAvailability(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.toggleAvailability(id));
    }

    @PostMapping(value = "/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Upload a vehicle image file (Admin only)")
    public ResponseEntity<Map<String, String>> uploadVehicleImage(@RequestParam("file") MultipartFile file) {
        String imageUrl = vehicleService.uploadVehicleImage(file);
        return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
    }
}
