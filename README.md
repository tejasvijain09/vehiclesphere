# VehicleSphere — Cloud-Native Wedding & Event Vehicle Rental Platform

> **Final-Year B.Tech Computer Science Capstone Project**  
> **Student:** Tejasvi, B.Tech CSE, Lovely Professional University  
> **Contact:** [tejasvijain09@gmail.com](mailto:tejasvijain09@gmail.com) | +91 98154 08330  
> A full-stack Indian wedding & special-event vehicle rental platform built with Java 21, Spring Boot 3, React 18, Tailwind CSS, and PostgreSQL.

---

## 🚀 Live Demo Quick Reference

- **Frontend Application:** [http://localhost:5173](http://localhost:5173)
- **Backend REST API:** [http://localhost:8080](http://localhost:8080)
- **Interactive Swagger UI:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **OpenAPI 3 JSON Schema:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

### 🔑 Demo Login Credentials

The application automatically seeds realistic demo data on initial startup. For your college viva demonstration, one-click quick-fill buttons are provided directly on the login form:

| Role | Email | Password | Name | Contact | Access Capabilities |
|---|---|---|---|---|---|
| **ADMIN** | `admin@vehiclesphere.com` | `Admin@123` | Platform Administrator | `+91 98154 08330` | Full dashboard KPIs, fleet CRUD, wedding booking approval/rejection/status workflows, customer directory |
| **CUSTOMER** | `gurpreet.singh@example.com` | `Customer@123` | Gurpreet Singh | `+91 98765 43210` | Vehicle browsing, live price calculator, celebration booking creation, reservation tracking, cancellation |
| **CUSTOMER** | `simran.kaur@example.com` | `Customer@123` | Simran Kaur | `+91 98765 43211` | Second customer account with existing sample bookings |
| **CUSTOMER** | `harleen.kaur@example.com` | `Customer@123` | Harleen Kaur | `+91 98155 44321` | Customer account with Indian phone number & Punjabi identity |

---

## 🚗 Wedding & Celebration Fleet Overview

VehicleSphere offers an exclusive luxury celebration fleet across major North Indian wedding destinations (Chandigarh, Delhi NCR, Ludhiana, Amritsar, Jalandhar, Jaipur, and Mumbai):

| Vehicle | Category | Daily Rate | Registration | Key Wedding Use Case |
|---|---|---|---|---|
| **Rolls-Royce Phantom VIII** | Luxury | ₹ 95,000 / day | `DL-01-RR-0007` | Royal wedding groom entry & starlight ceiling luxury |
| **Rolls-Royce Ghost Series II** | Luxury | ₹ 65,000 / day | `CH-01-RR-0001` | Crown jewel bridal send-off (Doli) & VIP arrivals |
| **Mercedes-Benz G63 AMG (G-Wagon)** | Luxury | ₹ 55,000 / day | `CH-01-GW-0063` | Thunderous V8 twin-turbo Baraat arrival showstopper |
| **Range Rover Autobiography LWB** | SUV | ₹ 45,000 / day | `CH-01-RR-1111` | Executive class comfort-plus rear lounge & convoy VIPs |
| **Toyota Land Cruiser 300 (LC300)** | SUV | ₹ 35,000 / day | `PB-10-LC-0009` | High-prestige twin-turbo V6 Baraat convoy leader |
| **Porsche Taycan 4S Luxury EV** | Electric | ₹ 30,000 / day | `DL-01-EV-9110` | Silent futuristic reception entry & cinematic video reels |
| **Mercedes-Benz S-Class S450 Maybach** | Luxury | ₹ 28,000 / day | `PB-10-LU-3001` | Executive bridal Doli send-off with Burmester 4D sound |
| **Land Rover Defender 110 X-Dynamic** | SUV | ₹ 25,000 / day | `PB-65-DF-0001` | The viral sensation of Punjabi Baraat escort convoys |
| **BMW 7 Series 740Li M Sport** | Sedan | ₹ 25,000 / day | `CH-01-WD-5510` | 31.3-inch 8K Theatre Screen & sky lounge couple arrival |
| **Audi A8L Quattro Matrix** | Sedan | ₹ 22,000 / day | `RJ-14-JP-6002` | Royal palace weddings in Rajasthan & VIP hospitality |
| **Mini Cooper S Convertible (Open-Top)** | Hatchback | ₹ 18,000 / day | `CH-01-MC-3321` | Open-roof floral shower entry & pre-wedding shoots |
| **Mercedes-Benz E-Class LWB E350d** | Sedan | ₹ 16,000 / day | `PB-10-MB-3500` | Reclining rear lounge seats for VIP elders and family |
| **Toyota Fortuner Legender 4x4** | SUV | ₹ 8,500 / day | `PB-02-AM-4491` | Heavy Baraat convoy muscle & celebration escort fleet |
| **Mahindra Scorpio-N Z8L 4x4** | SUV | ₹ 7,000 / day | `PB-08-SN-0008` | 'Big Daddy of SUVs' with Sony 3D sound for Baraat band |

---

## 🛠️ Technology Stack

### Backend
- **Language:** Java 21 (LTS)
- **Framework:** Spring Boot 3.3.4
- **Web:** Spring Web (RESTful API at `/api/v1`)
- **Persistence:** Spring Data JPA, Hibernate ORM
- **Security:** Spring Security 6, Stateless JWT (JSON Web Tokens), BCrypt password hashing
- **Validation:** Jakarta Bean Validation (Hibernate Validator)
- **Database Migrations:** Flyway 10
- **API Documentation:** Springdoc OpenAPI 2.6.0 with Swagger UI
- **Build Tool:** Apache Maven 3.9+

### Frontend
- **Library:** React 18
- **Bundler / Tooling:** Vite 5
- **Styling:** Tailwind CSS 3 (automotive dark navy & cobalt blue theme)
- **Routing:** React Router v6
- **HTTP Client:** Axios with JWT request & response interceptors
- **Icons:** Lucide React

### Database
- **Database Engine:** PostgreSQL 17 / 16 / 15
- **Schema Management:** Automated Flyway migrations (`db/migration/V1__init_schema.sql`)

---

## 📁 Project Directory Structure

```
VehicleSphere/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/vehiclesphere/
│   │   │   │   ├── auth/          # Authentication & registration controllers, DTOs
│   │   │   │   ├── booking/       # Booking engine, business rules, price calculator
│   │   │   │   ├── category/      # Vehicle category models & endpoints
│   │   │   │   ├── config/        # Security, CORS, Swagger, and Data Seeder
│   │   │   │   ├── dashboard/     # Admin and Customer telemetry statistics
│   │   │   │   ├── exception/     # Global error handling and HTTP responses
│   │   │   │   ├── security/      # JWT Provider, Auth Filter, UserDetailsService
│   │   │   │   ├── user/          # User entities, customer directory
│   │   │   │   ├── vehicle/       # Vehicle fleet repository and management
│   │   │   │   └── VehicleSphereApplication.java
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── db/migration/  # Flyway database migration scripts
│   │   └── test/                  # Unit and integration test suite
│   └── pom.xml
├── frontend/
│   ├── src/
│   │   ├── components/            # Navbar, Footer, ProtectedRoute, VehicleCard, StatusBadge
│   │   ├── context/               # AuthContext (JWT management, session state)
│   │   ├── pages/                 # Public & Customer pages (Home, Fleet, Detail, MyBookings)
│   │   │   └── admin/             # Admin pages (Dashboard, Fleet CRUD, Booking workflows, Users)
│   │   ├── services/              # Axios instance with Bearer interceptors
│   │   ├── App.jsx                # Route hierarchy
│   │   └── main.jsx
│   ├── package.json
│   ├── tailwind.config.js
│   └── vite.config.js
├── database/
│   ├── schema.sql                 # Reference SQL schema
│   └── seed.sql                   # Reference seed data
├── docs/
│   ├── ARCHITECTURE.md            # System architectural overview
│   ├── DATABASE_DESIGN.md         # Database schema & ER details
│   └── API_DOCUMENTATION.md       # API endpoint specification
├── .env.example
├── .gitignore
└── README.md
```

---

## ⚡ Quick Start & Setup Instructions

### 1. Prerequisites
Ensure you have the following installed on your machine:
- **Java 21 JDK** (`java -version`)
- **Maven 3.8+** (`mvn -version`)
- **Node.js 18+ and npm** (`node -v && npm -v`)
- **PostgreSQL** (`psql --version`)

---

### 2. Database Setup

1. Start your local PostgreSQL service:
   ```bash
   brew services start postgresql@17
   ```

2. Create the `vehiclesphere` database and user (if not already existing):
   ```bash
   psql -U postgres -c "CREATE DATABASE vehiclesphere;"
   psql -U postgres -c "CREATE USER vehiclesphere WITH PASSWORD 'vehiclesphere';"
   psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE vehiclesphere TO vehiclesphere;"
   ```

*(Default credentials configured in `application.yml`: Host `localhost:5432`, DB `vehiclesphere`, Username `vehiclesphere`, Password `vehiclesphere`).*

---

### 3. Backend Execution

1. Navigate to the `backend/` directory:
   ```bash
   cd backend
   ```

2. Compile and run unit tests:
   ```bash
   mvn test
   ```

3. Launch the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```
   *Or run the pre-built JAR:*
   ```bash
   java -jar target/vehiclesphere-backend-1.0.0.jar
   ```

4. The backend will automatically:
   - Run Flyway migrations.
   - Seed categories, 12 realistic Indian wedding vehicles, demo users, and bookings.
   - Start Tomcat on port `8080`.

---

### 4. Frontend Execution

1. In a new terminal window, navigate to the `frontend/` directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the Vite development server:
   ```bash
   npm run dev
   ```

4. Open your browser at [http://localhost:5173](http://localhost:5173).

---

## 🛡️ Business Rules & Key Logic

1. **Date Validation:**
   - Pickup date cannot be set in the past.
   - Return date must be strictly after the pickup date (minimum 1 day rental).

2. **Backend Price Calculation:**
   - Customers cannot manipulate rental prices on the frontend.
   - All charges are computed by the backend service using `BigDecimal`:
     $$\text{Total Amount (₹)} = \text{Rental Days} \times \text{Daily Rate (₹)}$$

3. **Overlapping Booking Prevention (Conflict Detection):**
   - If a vehicle already has an active or confirmed booking in the range $[P_1, R_1]$, any incoming reservation $[P_2, R_2]$ that satisfies:
     $$P_1 \le R_2 \quad \text{and} \quad R_1 \ge P_2$$
     is automatically rejected with **HTTP 409 Conflict**.

4. **Booking Lifecycle Workflow:**
   - Customer submits: `PENDING`
   - Admin approves: `CONFIRMED` (or `REJECTED`)
   - Vehicle delivered to venue: `ACTIVE`
   - Vehicle returned: `COMPLETED`
   - Customer can cancel eligible bookings while in `PENDING` or `CONFIRMED` states.

5. **Security Isolation:**
   - Customers can only access their own booking history.
   - Administrative endpoints under `/api/v1/admin/**` are strictly guarded by `hasRole('ADMIN')`.

---

## 📋 Demonstration Walkthrough (For College Viva)

1. **Public Showcase:**
   - Open [http://localhost:5173](http://localhost:5173).
   - Point out the Indian wedding rental hero section: *"Make Every Wedding Arrival Unforgettable."*
   - Show the 3-step celebration process: *Choose Your Wedding Vehicle* ➔ *Select Event Dates & Location* ➔ *Reserve for Your Celebration*.
   - Filter by Category (e.g. *Royal Luxury*, *Baraat SUVs*) or use the quick search bar.
2. **Customer Journey:**
   - Click **Sign In**, select **Customer Demo** (`gurpreet.singh@example.com`), and log in.
   - Click a vehicle (e.g. *BMW 530i M Sport*).
   - Notice the daily rate formatted as `₹ 18,000 / day` and station at *Chandigarh (Sector 17)*.
   - Select 3 days rental dates — watch the live calculation display `₹ 54,000`.
   - Submit booking request ➔ view booking in **My Bookings** with status `PENDING`.
3. **Admin Control:**
   - Sign out and click **Sign In** ➔ **Admin Demo** (`admin@vehiclesphere.com`).
   - Show the **Admin Dashboard** with revenue KPIs in Indian Rupees (`₹ 47,500`), fleet counts, and pending bookings.
   - Go to **Review Bookings**, locate the booking, and click **Approve** (transitions to `CONFIRMED`).
   - Demonstrate marking the rental as `ACTIVE` and `COMPLETED`.
   - Go to **Fleet Management**, showcase editing specifications, adding a new vehicle, or toggling availability.
4. **API Documentation:**
   - Open [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) to demonstrate the interactive OpenAPI documentation with JWT authorize capability.
