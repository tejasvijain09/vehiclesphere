# VehicleSphere - Requirements Document

## Project Overview
VehicleSphere is a full-stack vehicle rental management platform that allows customers to search and rent vehicles while enabling fleet managers and administrators to manage vehicles, bookings, and analytics.

---

# User Roles

## Customer
- Register account
- Login and logout
- Update profile
- Search vehicles
- View vehicle details
- Book vehicles
- Cancel bookings
- Return vehicles
- View booking history
- Add ratings and reviews

## Fleet Manager
- Login
- Add vehicles
- Update vehicle information
- Delete vehicles
- Upload vehicle images
- Manage vehicle availability
- View bookings
- Manage maintenance records

## Admin
- Login
- Manage users
- Manage vehicles
- View all bookings
- View reports and analytics
- Monitor platform activities

---

# Functional Requirements

## Authentication Module
- User Registration
- Login
- Logout
- JWT Authentication
- Refresh Tokens
- Password Encryption
- Role-Based Authorization

## Vehicle Module
- Add Vehicle
- Update Vehicle
- Delete Vehicle
- View Vehicle Details
- Upload Vehicle Images
- Manage Availability
- Vehicle Categorization

## Search Module
- Search by City
- Search by Vehicle Type
- Search by Price Range
- Search by Availability
- Pagination
- Sorting
- Filtering

## Booking Module
- Create Booking
- Cancel Booking
- Return Vehicle
- Booking History
- Calculate Rental Price

## Payment Module
- Mock Payment Integration
- Payment History
- Refund Processing

## Review Module
- Add Review
- Add Rating
- View Reviews

## Notification Module
- Booking Confirmation Email
- Booking Cancellation Email
- Reminder Notifications

## Dashboard Module
- Customer Dashboard
- Fleet Manager Dashboard
- Admin Dashboard
- Revenue Analytics
- Booking Statistics

---

# Non-Functional Requirements

## Security
- JWT Authentication
- Role-Based Access Control
- Password Encryption

## Performance
- Pagination
- Caching
- Optimized Queries

## Monitoring
- Logging
- Spring Actuator

## Deployment
- Docker
- Profiles
- Cloud Deployment