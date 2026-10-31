# VehicleSphere — Architecture Overview

## 1. System Architecture

VehicleSphere is designed as a **Modular Monolith** using Spring Boot 3 on the backend and React 18 on the frontend. It follows a clean layered pattern:

```
[ Frontend: React + Vite + Tailwind CSS ]
                   │
                   ▼ (REST API / JSON over HTTP)
[ Spring Boot 3 Backend / Security Filters (JWT) ]
                   │
                   ▼
┌────────────────────────────────────────────────────────┐
│                   Controllers Layer                   │
├────────────────────────────────────────────────────────┤
│                    Services Layer                      │
│ (Business logic, validation, price calculation)        │
├────────────────────────────────────────────────────────┤
│                  Repositories Layer                    │
│   (Spring Data JPA / Hibernate ORM / Criteria)         │
└────────────────────────────────────────────────────────┘
                   │
                   ▼
       [ PostgreSQL Database ]
```

## 2. Package Organization

```
com.vehiclesphere
├── config        # Security, CORS, OpenAPI Swagger, and Data Seeder
├── security      # JWT token provider, filter, and user details service
├── auth          # Registration, login authentication & DTOs
├── user          # User entity, repository, service, customer directory
├── vehicle       # Vehicle catalog, categories, availability management
├── booking       # Rental calculations, conflict checks, lifecycle workflows
├── category      # Vehicle category classification
├── dashboard     # Aggregated operational and customer metrics
└── exception     # Global REST exception handling and error responses
```

## 3. Security Design

- **Stateless Authentication**: Uses HMAC-SHA signed JSON Web Tokens (JWT) with configurable secret and expiration.
- **Password Security**: Passwords are cryptographically salted and hashed using BCrypt.
- **Role-Based Access Control (RBAC)**:
  - `CUSTOMER`: Can browse vehicles, calculate quotes, submit booking requests, manage their own reservations, and update profile.
  - `ADMIN`: Has full administrative privilege to manage vehicles, approve/reject bookings, mark rentals active/completed, and inspect system telemetry.
- **Authorization Enforcement**: Dual-layer defense with Spring Security `SecurityFilterChain` rules and method-level `@PreAuthorize("hasRole('ADMIN')")`.
