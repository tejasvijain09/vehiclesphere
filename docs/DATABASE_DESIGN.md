# VehicleSphere — Database Design & Schema

## 1. Entity-Relationship Overview

VehicleSphere uses PostgreSQL as its relational database management system. Migration scripts are versioned and managed with **Flyway**.

```
┌──────────────────┐               ┌──────────────────┐
│      USERS       │               │    CATEGORIES    │
├──────────────────┤               ├──────────────────┤
│ id (PK)          │               │ id (PK)          │
│ full_name        │               │ name (UNIQUE)    │
│ email (UNIQUE)   │               │ description      │
│ phone_number     │               │ created_at       │
│ password (BCrypt)│               └────────┬─────────┘
│ role             │                        │
│ created_at       │                        │ 1:N
│ updated_at       │                        │
└────────┬─────────┘                        ▼
         │                         ┌──────────────────┐
         │ 1:N                     │     VEHICLES     │
         │                         ├──────────────────┤
         │                         │ id (PK)          │
         │                         │ name             │
         │                         │ brand            │
         │                         │ model            │
         │                         │ category_id (FK) │
         │                         │ reg_number (UQ)  │
         │                         │ description      │
         │                         │ fuel_type        │
         │                         │ transmission     │
         │                         │ seating_capacity │
         │                         │ model_year       │
         │                         │ price_per_day    │
         │                         │ location         │
         │                         │ image_url        │
         │                         │ available        │
         │                         │ created_at       │
         │                         │ updated_at       │
         │                         └────────┬─────────┘
         │                                  │
         │                                  │ 1:N
         ▼                                  ▼
┌─────────────────────────────────────────────────────┐
│                      BOOKINGS                       │
├─────────────────────────────────────────────────────┤
│ id (PK)                                             │
│ booking_reference (UNIQUE)                          │
│ user_id (FK -> users.id)                            │
│ vehicle_id (FK -> vehicles.id)                      │
│ pickup_date (DATE)                                  │
│ return_date (DATE)                                  │
│ pickup_location (VARCHAR)                           │
│ number_of_days (INT)                                │
│ price_per_day (NUMERIC(10,2))                       │
│ total_amount (NUMERIC(12,2))                        │
│ status (PENDING, CONFIRMED, ACTIVE, COMPLETED, etc) │
│ notes (TEXT)                                        │
│ created_at (TIMESTAMP)                              │
│ updated_at (TIMESTAMP)                              │
└─────────────────────────────────────────────────────┘
```

## 2. Table Schemas

### `users`
Stores customer and administrator profile information.
- `id`: BIGSERIAL PRIMARY KEY
- `email`: VARCHAR(150) UNIQUE NOT NULL
- `password`: VARCHAR(255) NOT NULL (BCrypt hash)
- `role`: VARCHAR(20) NOT NULL (`CUSTOMER`, `ADMIN`)

### `categories`
Vehicle classifications (`Hatchback`, `Sedan`, `SUV`, `Luxury`, `Electric`).

### `vehicles`
Stores fleet inventory, specs, daily rental prices, and current availability status.
- `price_per_day`: NUMERIC(10, 2) NOT NULL
- `available`: BOOLEAN NOT NULL DEFAULT TRUE

### `bookings`
Stores reservation transactions and workflow states.
- Status Lifecycle:
  `PENDING` ➔ `CONFIRMED` ➔ `ACTIVE` ➔ `COMPLETED`
  (or `REJECTED` / `CANCELLED`)

## 3. Database Indexes

To maintain high query performance, indexes are established on:
- `idx_users_email`
- `idx_vehicles_category`
- `idx_vehicles_available`
- `idx_vehicles_brand`
- `idx_bookings_user`
- `idx_bookings_vehicle`
- `idx_bookings_status`
- `idx_bookings_dates` (composite for overlap validation)
