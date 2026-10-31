package com.vehiclesphere.wishlist;

import com.vehiclesphere.exception.ResourceNotFoundException;
import com.vehiclesphere.user.User;
import com.vehiclesphere.user.UserRepository;
import com.vehiclesphere.vehicle.Vehicle;
import com.vehiclesphere.vehicle.VehicleRepository;
import com.vehiclesphere.vehicle.VehicleService;
import com.vehiclesphere.vehicle.dto.VehicleDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final VehicleService vehicleService;

    @Transactional
    public void addToWishlist(Long userId, Long vehicleId) {
        if (wishlistRepository.existsByUserIdAndVehicleId(userId, vehicleId)) {
            log.info("Vehicle {} is already in wishlist for user {}", vehicleId, userId);
            return;
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + vehicleId));

        Wishlist wishlist = Wishlist.builder()
                .user(user)
                .vehicle(vehicle)
                .build();

        wishlistRepository.save(wishlist);
        log.info("Vehicle {} added to wishlist for user {}", vehicleId, userId);
    }

    @Transactional
    public void removeFromWishlist(Long userId, Long vehicleId) {
        wishlistRepository.deleteByUserIdAndVehicleId(userId, vehicleId);
        log.info("Vehicle {} removed from wishlist for user {}", vehicleId, userId);
    }

    @Transactional(readOnly = true)
    public List<VehicleDto> getMyWishlist(Long userId) {
        return wishlistRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(w -> vehicleService.mapToDto(w.getVehicle()))
                .toList();
    }

    @Transactional(readOnly = true)
    public boolean isVehicleWishlisted(Long userId, Long vehicleId) {
        return wishlistRepository.existsByUserIdAndVehicleId(userId, vehicleId);
    }
}
