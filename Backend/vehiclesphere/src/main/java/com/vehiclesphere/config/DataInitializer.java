package com.vehiclesphere.config;

import com.vehiclesphere.entity.Role;
import com.vehiclesphere.enums.RoleType;
import com.vehiclesphere.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {

        for (RoleType roleType : RoleType.values()) {

            roleRepository
                    .findByName(roleType)
                    .orElseGet(() -> roleRepository.save(Role.builder()
                                            .name(roleType)
                                            .build()
                            )
                    );
        }
    }
}