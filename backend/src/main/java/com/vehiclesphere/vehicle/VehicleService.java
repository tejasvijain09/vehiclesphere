package com.vehiclesphere.vehicle;

import com.vehiclesphere.category.Category;
import com.vehiclesphere.category.CategoryRepository;
import com.vehiclesphere.exception.BadRequestException;
import com.vehiclesphere.exception.ConflictException;
import com.vehiclesphere.exception.ResourceNotFoundException;
import com.vehiclesphere.vehicle.dto.CreateVehicleRequest;
import com.vehiclesphere.vehicle.dto.UpdateVehicleRequest;
import com.vehiclesphere.vehicle.dto.VehicleDto;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<VehicleDto> searchVehicles(String search, Long categoryId, String fuelType, BigDecimal maxPrice, Boolean availableOnly) {
        Specification<Vehicle> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(search)) {
                String trimmed = search.trim().toLowerCase();
                String[] tokens = trimmed.split("\\s+");
                for (String token : tokens) {
                    if (!token.isBlank()) {
                        String pattern = "%" + token + "%";
                        predicates.add(cb.or(
                                cb.like(cb.lower(root.get("name")), pattern),
                                cb.like(cb.lower(root.get("brand")), pattern),
                                cb.like(cb.lower(root.get("model")), pattern),
                                cb.like(cb.lower(root.get("location")), pattern),
                                cb.like(cb.lower(root.get("description")), pattern),
                                cb.like(cb.lower(root.get("registrationNumber")), pattern),
                                cb.like(cb.lower(root.get("category").get("name")), pattern)
                        ));
                    }
                }
            }

            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }

            if (StringUtils.hasText(fuelType)) {
                predicates.add(cb.equal(cb.lower(root.get("fuelType")), fuelType.toLowerCase().trim()));
            }

            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("pricePerDay"), maxPrice));
            }

            if (Boolean.TRUE.equals(availableOnly)) {
                predicates.add(cb.isTrue(root.get("available")));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return vehicleRepository.findAll(spec)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<VehicleDto> getAllVehicles() {
        return vehicleRepository.findAll().stream().map(this::mapToDto).toList();
    }

    @Transactional(readOnly = true)
    public VehicleDto getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));
        return mapToDto(vehicle);
    }

    @Transactional
    public VehicleDto createVehicle(CreateVehicleRequest request) {
        if (vehicleRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {
            throw new ConflictException("Vehicle with registration number already exists: " + request.getRegistrationNumber());
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        Vehicle vehicle = Vehicle.builder()
                .name(request.getName())
                .brand(request.getBrand())
                .model(request.getModel())
                .category(category)
                .registrationNumber(request.getRegistrationNumber())
                .description(request.getDescription())
                .fuelType(request.getFuelType())
                .transmission(request.getTransmission())
                .seatingCapacity(request.getSeatingCapacity())
                .modelYear(request.getModelYear())
                .pricePerDay(request.getPricePerDay())
                .location(request.getLocation())
                .imageUrl(request.getImageUrl())
                .available(request.getAvailable() != null ? request.getAvailable() : true)
                .build();

        return mapToDto(vehicleRepository.save(vehicle));
    }

    @Transactional
    public VehicleDto updateVehicle(Long id, UpdateVehicleRequest request) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));

        if (vehicleRepository.existsByRegistrationNumberAndIdNot(request.getRegistrationNumber(), id)) {
            throw new ConflictException("Vehicle with registration number already exists: " + request.getRegistrationNumber());
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        vehicle.setName(request.getName());
        vehicle.setBrand(request.getBrand());
        vehicle.setModel(request.getModel());
        vehicle.setCategory(category);
        vehicle.setRegistrationNumber(request.getRegistrationNumber());
        vehicle.setDescription(request.getDescription());
        vehicle.setFuelType(request.getFuelType());
        vehicle.setTransmission(request.getTransmission());
        vehicle.setSeatingCapacity(request.getSeatingCapacity());
        vehicle.setModelYear(request.getModelYear());
        vehicle.setPricePerDay(request.getPricePerDay());
        vehicle.setLocation(request.getLocation());
        vehicle.setImageUrl(request.getImageUrl());
        if (request.getAvailable() != null) {
            vehicle.setAvailable(request.getAvailable());
        }

        return mapToDto(vehicleRepository.save(vehicle));
    }

    @Transactional
    public void deleteVehicle(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));
        vehicleRepository.delete(vehicle);
    }

    @Transactional
    public VehicleDto toggleAvailability(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));
        vehicle.setAvailable(!vehicle.getAvailable());
        return mapToDto(vehicleRepository.save(vehicle));
    }

    public String uploadVehicleImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Uploaded file cannot be empty");
        }
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png") && !contentType.equals("image/webp"))) {
            throw new BadRequestException("Only JPEG, PNG, and WebP image formats are allowed");
        }
        if (file.getSize() > 5 * 1024 * 1024) { // 5MB limit
            throw new BadRequestException("Image file size must not exceed 5MB");
        }

        try {
            String extension = contentType.contains("png") ? "png" : (contentType.contains("webp") ? "webp" : "jpg");
            String filename = "custom-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8) + "." + extension;

            Path uploadDir = Paths.get("frontend/public/images/vehicles").toAbsolutePath().normalize();
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            Path targetPath = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            log.info("Stored vehicle image at {}", targetPath);
            return "/images/vehicles/" + filename;
        } catch (IOException e) {
            log.error("Failed to store vehicle image", e);
            throw new RuntimeException("Could not store image file: " + e.getMessage());
        }
    }

    public VehicleDto mapToDto(Vehicle vehicle) {
        return VehicleDto.builder()
                .id(vehicle.getId())
                .name(vehicle.getName())
                .brand(vehicle.getBrand())
                .model(vehicle.getModel())
                .categoryId(vehicle.getCategory() != null ? vehicle.getCategory().getId() : null)
                .categoryName(vehicle.getCategory() != null ? vehicle.getCategory().getName() : null)
                .registrationNumber(vehicle.getRegistrationNumber())
                .description(vehicle.getDescription())
                .fuelType(vehicle.getFuelType())
                .transmission(vehicle.getTransmission())
                .seatingCapacity(vehicle.getSeatingCapacity())
                .modelYear(vehicle.getModelYear())
                .pricePerDay(vehicle.getPricePerDay())
                .location(vehicle.getLocation())
                .imageUrl(vehicle.getImageUrl())
                .available(vehicle.getAvailable())
                .createdAt(vehicle.getCreatedAt())
                .build();
    }
}
