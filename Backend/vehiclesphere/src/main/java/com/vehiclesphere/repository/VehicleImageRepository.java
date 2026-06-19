package com.vehiclesphere.repository;

import com.vehiclesphere.entity.VehicleImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VehicleImageRepository extends JpaRepository<VehicleImage, UUID> {
}
