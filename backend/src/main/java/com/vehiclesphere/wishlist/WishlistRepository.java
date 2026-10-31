package com.vehiclesphere.wishlist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<Wishlist> findByUserIdAndVehicleId(Long userId, Long vehicleId);
    boolean existsByUserIdAndVehicleId(Long userId, Long vehicleId);
    void deleteByUserIdAndVehicleId(Long userId, Long vehicleId);
}
