package com.vehiclesphere.repository;

import com.vehiclesphere.entity.Vehicle;
import com.vehiclesphere.enums.VehicleStatus;
import com.vehiclesphere.enums.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

    List<Vehicle> findByCity(String city);
    List<Vehicle> findByStatus(VehicleStatus status);
    List<Vehicle> findByVehicleType(VehicleType vehicleType);
}
