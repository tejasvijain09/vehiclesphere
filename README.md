# VehicleSphere — Cloud-Native Wedding & Event Vehicle Rental Platform

> **Final-Year B.Tech Computer Science Capstone Project**  
> **Student:** Tejasvi, B.Tech CSE, Lovely Professional University  
> **Contact:** [tejasvijain09@gmail.com](mailto:tejasvijain09@gmail.com) | +91 98154 08330  
> A full-stack Indian wedding & special-event vehicle rental platform built with Java 21, Spring Boot 3, React 18, Tailwind CSS, PostgreSQL, Razorpay Test Gateway, and OpenPDF.

---

## 🚀 Live Demo Quick Reference

- **Frontend Application:** [http://localhost:5173](http://localhost:5173)
- **Admin Portal (Auto-Redirects):** [http://localhost:5173/admin](http://localhost:5173/admin) ➔ `/admin/dashboard`
- **Backend REST API:** [http://localhost:8080](http://localhost:8080)
- **Interactive Swagger UI:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **OpenAPI 3 JSON Schema:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

### 🔑 Demo Login Credentials

The application automatically seeds realistic demo data on initial startup. For your college viva demonstration, one-click quick-fill buttons are provided directly on the login form:

| Role | Email | Password | Name | Contact | Access Capabilities |
|---|---|---|---|---|---|
| **ADMIN** | `admin@vehiclesphere.com` | `Admin@123` | Platform Administrator | `+91 98154 08330` | Full dashboard KPIs, revenue analytics, fleet CRUD, vehicle image uploads, booking approval/rejection workflows, customer directory |
| **CUSTOMER** | `gurpreet.singh@example.com` | `Customer@123` | Gurpreet Singh | `+91 98765 43210` | Vehicle browsing, live price calculation, Razorpay payment, wishlist curation, completed trip reviews, PDF invoice download |
| **CUSTOMER** | `simran.kaur@example.com` | `Customer@123` | Simran Kaur | `+91 98765 43211` | Customer account with existing sample reservations |
| **CUSTOMER** | `harleen.kaur@example.com` | `Customer@123` | Harleen Kaur | `+91 98155 44321` | Customer account with Indian phone number & Punjabi identity |

---

## 🌟 Advanced Features Overview

### 1. 💳 Razorpay Payment Gateway (Test Mode)
- **Backend Order Creation:** Securely calculates payable amounts in paise on the backend (`totalAmount * 100`) and generates orders using the Razorpay Java SDK.
- **HMAC-SHA256 Signature Verification:** Verifies payment authenticity via cryptographic signature checks before marking records as `PAID`. Never trusts untrusted frontend reports.
- **Duplicate Payment Prevention:** Rejects duplicate payment attempts for already settled bookings.
- **Built-in Test Mode Simulation:** Seamlessly operates in test mode even when external keys are unconfigured, allowing examiners to test both payment success and cancellation/failure simulations.
- **Razorpay Standard Test Cards:**
  - Card Number: `4012 0000 0000 0002`
  - Expiry: Any future date (e.g. `12/28`)
  - CVV: `123`
  - OTP: `123456`
- **Payment History:** Complete customer transaction ledger displaying Order IDs, Payment IDs, timestamps, and status badges.

### 2. 📄 Downloadable PDF GST Rental Tax Invoice
- **Automated OpenPDF Engine:** Dynamically generates GST-compliant vehicle rental tax invoices.
- **Indian GST Breakdown:** Includes Company GSTIN (`03AAAAA0000A1Z5`), SAC Code (`996601`), Taxable Subtotal, CGST (9.0%), SGST (9.0%), Total GST (18.0%), and Grand Total.
- **Branded & Secured:** Includes VehicleSphere branding, customer details, booking reference, rental dates, daily rates, calculated totals, and transaction references.
- **Access Control:** Customers can only download invoices for their own reservations; administrators can download for any reservation.

### 3. ⭐ Customer Reviews & Ratings System
- **Verified Trip Reviews:** Only customers who have completed their vehicle rentals (`COMPLETED` status) can submit reviews.
- **Interactive Ratings:** 1 to 5 star rating with customer commentary.
- **Duplicate Prevention:** Enforces a unique constraint preventing multiple reviews per booking.
- **Fleet Showcase:** Displays dynamic star rating summaries, average ratings, and customer feedback on vehicle detail pages.

### 4. 📊 Advanced Admin Dashboard & Analytics
- **Reconciled Revenue:** Total revenue metric strictly aggregates successfully paid bookings (`payment_status = 'PAID'`).
- **Dynamic Trend Charts:** Responsive monthly revenue and booking volume charts over time.
- **Status Distribution:** Visual breakdown of platform reservations (Pending, Confirmed, Active, Completed, Cancelled).
- **Top 5 Rented Vehicles:** Leaderboard ranking fleet vehicles by rental frequency.

### 5. 🖼️ Vehicle Image Management & Live Preview
- **Real-Time Preview:** Add/Edit vehicle modal features an instant live image preview box.
- **Validated Multipart File Upload:** Admins can directly upload vehicle images (JPEG, PNG, WebP up to 5MB) stored safely outside the database.

### 6. ❤️ Customer Wishlist (Saved Vehicles)
- **Persisted in PostgreSQL:** Wishlists are stored in the database with unique constraints.
- **One-Click Heart Toggle:** Heart button on vehicle cards and detail pages for quick saving.
- **Dedicated Wishlist View:** Customers have a dedicated `/wishlist` page to curate and compare their celebration choices.

### 7. 📧 Asynchronous Email Notifications
- **Spring Boot Mail Integration:** Background async worker (`@Async`) sends branded HTML notifications for registration, booking requests, confirmations, cancellations, and payment receipts.
- **Development Fallback Mode:** When SMTP credentials are not configured (`app.mail.enabled=false`), all email events are logged cleanly to the console without interrupting requests.

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
- **Payments:** Razorpay Java SDK 1.4.6
- **PDF Generation:** OpenPDF 1.3.39
- **Email:** Spring Boot Starter Mail with asynchronous background dispatching (`@Async`)
- **Validation:** Jakarta Bean Validation (Hibernate Validator)
- **Database Migrations:** Flyway 10 (V1 schema, V2 advanced features)
- **API Documentation:** Springdoc OpenAPI 2.6.0 with Swagger UI
- **Build Tool:** Apache Maven 3.9+

### Frontend
- **Library:** React 18
- **Bundler / Tooling:** Vite 5
- **Styling:** Tailwind CSS 3
- **Routing:** React Router v6
- **HTTP Client:** Axios with JWT request & response interceptors
- **Payment UI:** Razorpay Checkout JS integration with fallback test simulator
- **Icons:** Lucide React

### Database
- **Database Engine:** PostgreSQL 17 / 16 / 15
- **Flyway Migrations:** 
  - `V1__init_schema.sql` (Users, Categories, Vehicles, Bookings)
  - `V2__advanced_features.sql` (Payments, Reviews, Wishlists, payment_status column)

---

## 🏃 Running the Project Locally

### 1. Prerequisites
- **Java 21 JDK**
- **Node.js 18+ and npm**
- **PostgreSQL 15+**
- **Maven 3.8+**

### 2. Database Setup
```bash
createdb vehiclesphere
```

### 3. Backend Execution
```bash
cd backend
mvn clean package -DskipTests
java -jar target/vehiclesphere-backend-1.0.0.jar
```
Backend starts on `http://localhost:8080`.

### 4. Frontend Execution
```bash
cd frontend
npm install
npm run dev
```
Frontend starts on `http://localhost:5173`.

---

## 📋 Demonstration Walkthrough (For College Viva)

1. **Customer Booking & Razorpay Payment:**
   - Log in as Customer `gurpreet.singh@example.com` (password: `Customer@123`).
   - Browse fleet, select a vehicle (e.g. *Mercedes S-Class*), and reserve celebration dates.
   - Go to **My Bookings**, notice the **UNPAID** badge, and click **Pay with Razorpay**.
   - Complete authorization in the modal ➔ Booking status automatically transitions to **CONFIRMED** and payment status becomes **PAID**.
2. **Download GST Tax Invoice:**
   - In **My Bookings**, click **Tax Invoice** on any confirmed/completed booking.
   - Observe the instantly downloaded branded PDF containing customer details, invoice numbers, breakdown, and payment transaction reference.
3. **Wishlist Management:**
   - Click the heart icon on any vehicle card to save it.
   - Navigate to **Wishlist** from the top bar to inspect your saved celebration collection.
4. **Customer Review Submission:**
   - On completed booking records, click **Review Vehicle**.
   - Submit a 5-star rating with personal feedback.
   - Visit the vehicle's details page to observe the newly updated average rating and customer review card.
5. **Admin Analytics & Management:**
   - Log in as Admin `admin@vehiclesphere.com` (password: `Admin@123`).
   - Inspect the **Admin Dashboard** showing paid revenue (strictly aggregated from successful payments), monthly revenue trends, status distribution, and top rented vehicles.
   - Navigate to **Vehicles**, click **Add Vehicle** or **Edit**, and test the **Live Photo Preview** and **Upload Image** features.
6. **API Documentation:**
   - Open [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) to demonstrate all 25+ secured endpoints and interactive documentation.
