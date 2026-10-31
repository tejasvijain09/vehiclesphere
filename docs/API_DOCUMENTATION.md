# VehicleSphere — REST API Documentation

Base URL: `/api/v1`
Interactive Swagger UI: `http://localhost:8080/swagger-ui/index.html`

## 1. Authentication Endpoints

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/api/v1/auth/register` | Public | Register customer account |
| `POST` | `/api/v1/auth/login` | Public | Login and receive Bearer JWT |

## 2. Vehicle Endpoints

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/v1/vehicles` | Public | Search and filter vehicles |
| `GET` | `/api/v1/vehicles/{id}` | Public | Retrieve vehicle specifications by ID |
| `POST` | `/api/v1/vehicles` | ADMIN | Add new vehicle to fleet |
| `PUT` | `/api/v1/vehicles/{id}` | ADMIN | Update vehicle specifications |
| `DELETE` | `/api/v1/vehicles/{id}` | ADMIN | Delete vehicle from fleet |
| `PATCH` | `/api/v1/vehicles/{id}/toggle-availability` | ADMIN | Toggle availability |

## 3. Booking Endpoints

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/api/v1/bookings` | CUSTOMER | Create reservation request |
| `GET` | `/api/v1/bookings/my` | CUSTOMER | View logged-in customer's bookings |
| `GET` | `/api/v1/bookings/{id}` | Authenticated | View booking details |
| `PATCH` | `/api/v1/bookings/{id}/cancel` | Authenticated | Cancel eligible booking |
| `GET` | `/api/v1/bookings/calculate-price` | Public | Calculate rental quote and check availability |

## 4. Administrative Endpoints

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/v1/admin/dashboard` | ADMIN | Aggregate platform and revenue telemetry |
| `GET` | `/api/v1/admin/bookings` | ADMIN | View all platform bookings |
| `PATCH` | `/api/v1/admin/bookings/{id}/status` | ADMIN | Update booking status (`CONFIRMED`, `REJECTED`, `ACTIVE`, `COMPLETED`) |
| `GET` | `/api/v1/admin/users` | ADMIN | View all registered customer accounts |

## 5. User Profile Endpoints

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/v1/users/profile` | Authenticated | View current user profile |
| `PUT` | `/api/v1/users/profile` | Authenticated | Update user name and phone |
