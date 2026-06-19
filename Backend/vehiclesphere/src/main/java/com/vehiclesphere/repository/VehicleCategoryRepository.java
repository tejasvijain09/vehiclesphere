package com.vehiclesphere.repository;

import com.vehiclesphere.entity.VehicleCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VehicleCategoryRepository extends JpaRepository<VehicleCategory, UUID> {
}
