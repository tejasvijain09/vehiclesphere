package com.vehiclesphere.mapper;

import com.vehiclesphere.dto.request.vehicle.CreateVehicleRequest;
import com.vehiclesphere.dto.response.vehicle.VehicleResponse;
import com.vehiclesphere.entity.Vehicle;
import com.vehiclesphere.entity.VehicleCategory;
import org.springframework.stereotype.Component;

@Component
public class VehicleMapper {

    public Vehicle toEntity(
            CreateVehicleRequest request,
            VehicleCategory category
    ) {

        Vehicle vehicle = new Vehicle();

        vehicle.setRegistrationNumber(
                request.getRegistrationNumber()
        );
        vehicle.setBrand(
                request.getBrand()
        );
        vehicle.setModel(
                request.getModel()
        );
        vehicle.setManufacturingYear(
                request.getManufacturingYear()
        );
        vehicle.setColor(
                request.getColor()
        );
        vehicle.setVehicleType(
                request.getVehicleType()
        );
        vehicle.setFuelType(
                request.getFuelType()
        );
        vehicle.setSeatingCapacity(
                request.getSeatingCapacity()
        );
        vehicle.setCity(
                request.getCity()
        );
        vehicle.setLocation(
                request.getLocation()
        );
        vehicle.setPricePerDay(
                request.getPricePerDay()
        );
        vehicle.setDescription(
                request.getDescription()
        );

        vehicle.setCategory(category);

        return vehicle;
    }

    public VehicleResponse toResponse(
            Vehicle vehicle
    ) {

        return VehicleResponse.builder()
                .id(vehicle.getId())
                .registrationNumber(
                        vehicle.getRegistrationNumber()
                )
                .brand(
                        vehicle.getBrand()
                )
                .model(
                        vehicle.getModel()
                )
                .manufacturingYear(
                        vehicle.getManufacturingYear()
                )
                .color(
                        vehicle.getColor()
                )
                .vehicleType(
                        vehicle.getVehicleType()
                )
                .fuelType(
                        vehicle.getFuelType()
                )
                .seatingCapacity(
                        vehicle.getSeatingCapacity()
                )
                .city(
                        vehicle.getCity()
                )
                .location(
                        vehicle.getLocation()
                )
                .pricePerDay(
                        vehicle.getPricePerDay()
                )
                .averageRating(
                        vehicle.getAverageRating()
                )
                .status(
                        vehicle.getStatus()
                )
                .description(
                        vehicle.getDescription()
                )
                .build();
    }
}