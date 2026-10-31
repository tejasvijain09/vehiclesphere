package com.vehiclesphere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class VehicleSphereApplication {

    public static void main(String[] args) {
        SpringApplication.run(VehicleSphereApplication.class, args);
    }
}
