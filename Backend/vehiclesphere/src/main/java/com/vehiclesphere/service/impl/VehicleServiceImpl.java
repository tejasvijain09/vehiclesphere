package com.vehiclesphere.service.impl;

import com.vehiclesphere.dto.request.vehicle.CreateVehicleRequest;
import com.vehiclesphere.dto.request.vehicle.UpdateVehicleRequest;
import com.vehiclesphere.dto.request.vehicle.VehicleSearchRequest;
import com.vehiclesphere.dto.response.vehicle.VehicleResponse;
import com.vehiclesphere.mapper.VehicleMapper;
import com.vehiclesphere.repository.VehicleCategoryRepository;
import com.vehiclesphere.repository.VehicleRepository;
import com.vehiclesphere.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl
        implements VehicleService {

    private final VehicleRepository vehicleRepository;

    private final VehicleCategoryRepository
            vehicleCategoryRepository;

    private final VehicleMapper vehicleMapper;

    @Override
    public VehicleResponse createVehicle(
            CreateVehicleRequest request
    ) {
        return null;
    }

    @Override
    public VehicleResponse getVehicleById(
            UUID vehicleId
    ) {
        return null;
    }

    @Override
    public VehicleResponse updateVehicle(
            UUID vehicleId,
            UpdateVehicleRequest request
    ) {
        return null;
    }

    @Override
    public void deleteVehicle(
            UUID vehicleId
    ) {

    }

    @Override
    public Page<VehicleResponse> searchVehicles(
            VehicleSearchRequest request,
            int page,
            int size,
            String sortBy,
            String sortDirection
    ) {
        return null;
    }
}