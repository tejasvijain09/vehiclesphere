# VehicleSphere - Entities

# User
Represents all platform users.

Attributes:
- id
- name
- email
- password
- phone
- status
- createdAt
- updatedAt

---

# Role
Represents user permissions.

Attributes:
- id
- name
- description

Examples:
- ADMIN
- FLEET_MANAGER
- CUSTOMER

---

# Vehicle
Represents vehicles available for rent.

Attributes:
- id
- vehicleNumber
- brand
- model
- description
- vehicleType
- fuelType
- seatingCapacity
- city
- pricePerDay
- averageRating
- status
- createdAt
- updatedAt

---

# VehicleCategory
Represents vehicle categories.

Attributes:
- id
- name
- description

Examples:
- Sedan
- SUV
- Hatchback
- Sports
- Electric

---

# VehicleImage
Stores vehicle images.

Attributes:
- id
- imageUrl
- isPrimary

---

# Booking
Represents rental transactions.

Attributes:
- id
- startDate
- endDate
- pickupLocation
- dropLocation
- totalAmount
- status
- createdAt

---

# Payment
Represents booking payments.

Attributes:
- id
- amount
- paymentMethod
- transactionId
- paymentStatus
- paidAt

---

# Review
Represents ratings and feedback.

Attributes:
- id
- rating
- comment
- createdAt

---

# MaintenanceRecord
Represents vehicle maintenance history.

Attributes:
- id
- description
- serviceDate
- nextServiceDate
- cost
- status

---

# Notification
Represents user notifications.

Attributes:
- id
- title
- message
- type
- isRead
- createdAt

---

# RefreshToken
Stores refresh tokens.

Attributes:
- id
- token
- expiryDate
- createdAt