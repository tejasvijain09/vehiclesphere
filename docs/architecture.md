# VehicleSphere - System Architecture

# Architecture Style
Monolithic Full-Stack Application

---

# High Level Architecture

Frontend (React)
↓
REST APIs
↓
Spring Boot Backend
↓
Hibernate / Spring Data JPA
↓
PostgreSQL Database

Additional Components:
- Redis Cache
- Spring Security
- Spring Actuator
- Docker

---

# Backend Layers

Controller Layer
↓
Service Layer
↓
Repository Layer
↓
Database

---

# Design Patterns

## Layered Architecture
Controller → Service → Repository

## DTO Pattern
Used for request and response objects.

## Builder Pattern
Used for creating DTOs and entities.

## Factory Pattern
Used for payment and notification creation.

## Strategy Pattern
Used for rental pricing calculations.

## Specification Pattern
Used for dynamic searching and filtering.

---

# Modules

1. Authentication Module
2. User Management Module
3. Vehicle Management Module
4. Search Module
5. Booking Module
6. Payment Module
7. Review Module
8. Notification Module
9. Dashboard Module
10. Admin Module

---

# Security

Authentication:
JWT

Authorization:
Role-Based Access Control

Roles:
- CUSTOMER
- FLEET_MANAGER
- ADMIN

---

# Caching Strategy

Redis:
- Popular vehicles
- Dashboard statistics
- Vehicle search results

---

# Monitoring

- Spring Actuator
- Logging
- Exception Handling

---

# Deployment

Frontend:
React

Backend:
Spring Boot

Database:
PostgreSQL

Containerization:
Docker Compose