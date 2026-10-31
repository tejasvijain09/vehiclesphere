package com.vehiclesphere.wishlist;

import com.vehiclesphere.security.UserPrincipal;
import com.vehiclesphere.vehicle.dto.VehicleDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/wishlist")
@RequiredArgsConstructor
@Tag(name = "Customer Wishlist", description = "Endpoints for managing favourite and saved vehicles")
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/{vehicleId}")
    @Operation(summary = "Add a vehicle to current customer's favourites")
    public ResponseEntity<Map<String, String>> addToWishlist(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long vehicleId
    ) {
        wishlistService.addToWishlist(principal.getId(), vehicleId);
        return ResponseEntity.ok(Map.of("message", "Vehicle added to wishlist successfully"));
    }

    @DeleteMapping("/{vehicleId}")
    @Operation(summary = "Remove a vehicle from current customer's favourites")
    public ResponseEntity<Map<String, String>> removeFromWishlist(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long vehicleId
    ) {
        wishlistService.removeFromWishlist(principal.getId(), vehicleId);
        return ResponseEntity.ok(Map.of("message", "Vehicle removed from wishlist successfully"));
    }

    @GetMapping
    @Operation(summary = "Get list of vehicles in current customer's favourites")
    public ResponseEntity<List<VehicleDto>> getMyWishlist(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return ResponseEntity.ok(wishlistService.getMyWishlist(principal.getId()));
    }

    @GetMapping("/check/{vehicleId}")
    @Operation(summary = "Check if a vehicle is in customer's favourites")
    public ResponseEntity<Map<String, Boolean>> checkIsWishlisted(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long vehicleId
    ) {
        boolean wishlisted = wishlistService.isVehicleWishlisted(principal.getId(), vehicleId);
        return ResponseEntity.ok(Map.of("isWishlisted", wishlisted));
    }
}
