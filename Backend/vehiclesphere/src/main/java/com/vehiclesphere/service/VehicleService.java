package com.vehiclesphere.service;

import com.vehiclesphere.dto.request.vehicle.CreateVehicleRequest;
import com.vehiclesphere.dto.request.vehicle.UpdateVehicleRequest;
import com.vehiclesphere.dto.request.vehicle.VehicleSearchRequest;
import com.vehiclesphere.dto.response.vehicle.VehicleResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface VehicleService {

    VehicleResponse createVehicle(
            CreateVehicleRequest request
    );

    VehicleResponse getVehicleById(
            UUID vehicleId
    );

    VehicleResponse updateVehicle(
            UUID vehicleId,
            UpdateVehicleRequest request
    );

    void deleteVehicle(
            UUID vehicleId
    );

    Page<VehicleResponse> searchVehicles(
            VehicleSearchRequest request,
            int page,
            int size,
            String sortBy,
            String sortDirection
    );
}