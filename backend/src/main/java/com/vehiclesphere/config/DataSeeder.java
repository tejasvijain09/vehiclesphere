package com.vehiclesphere.config;

import com.vehiclesphere.booking.Booking;
import com.vehiclesphere.booking.BookingRepository;
import com.vehiclesphere.booking.BookingStatus;
import com.vehiclesphere.category.Category;
import com.vehiclesphere.category.CategoryRepository;
import com.vehiclesphere.user.Role;
import com.vehiclesphere.user.User;
import com.vehiclesphere.user.UserRepository;
import com.vehiclesphere.vehicle.Vehicle;
import com.vehiclesphere.vehicle.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final VehicleRepository vehicleRepository;
    private final BookingRepository bookingRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        seedCategories();
        seedUsers();
        seedVehicles();
        seedBookings();
    }

    private void seedCategories() {
        if (categoryRepository.count() == 0) {
            log.info("Seeding Categories for Wedding & Event Fleet...");
            categoryRepository.saveAll(List.of(
                    Category.builder().name("Hatchback").description("Agile support cars for city wedding errands, bridal salon transfers, and logistics.").build(),
                    Category.builder().name("Sedan").description("Elegant classic sedans ideal for bridal entries, couple departures, and guest hospitality.").build(),
                    Category.builder().name("SUV").description("Commanding SUVs perfect for grand Baraat processions, VIP guests, and destination wedding convoys.").build(),
                    Category.builder().name("Luxury").description("Ultra-premium executive cars for groom entry, bridal send-off, and high-profile wedding receptions.").build(),
                    Category.builder().name("Electric").description("Cutting-edge luxury electric vehicles offering silent comfort for wedding shoots and modern celebrations.").build()
            ));
        }
    }

    private void seedUsers() {
        if (userRepository.count() == 0) {
            log.info("Seeding Initial Users...");
            // Admin
            User admin = User.builder()
                    .fullName("Platform Administrator")
                    .email("admin@vehiclesphere.com")
                    .phoneNumber("+91 98154 08330")
                    .password(passwordEncoder.encode("Admin@123"))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);

            // Customers
            User customer1 = User.builder()
                    .fullName("Gurpreet Singh")
                    .email("gurpreet.singh@example.com")
                    .phoneNumber("+91 98765 43210")
                    .password(passwordEncoder.encode("Customer@123"))
                    .role(Role.CUSTOMER)
                    .build();

            User customer2 = User.builder()
                    .fullName("Simran Kaur")
                    .email("simran.kaur@example.com")
                    .phoneNumber("+91 98765 43211")
                    .password(passwordEncoder.encode("Customer@123"))
                    .role(Role.CUSTOMER)
                    .build();

            User customer3 = User.builder()
                    .fullName("Jaswinder Singh")
                    .email("jaswinder.singh@example.com")
                    .phoneNumber("+91 98765 43212")
                    .password(passwordEncoder.encode("Customer@123"))
                    .role(Role.CUSTOMER)
                    .build();

            User customer4 = User.builder()
                    .fullName("Harleen Kaur")
                    .email("harleen.kaur@example.com")
                    .phoneNumber("+91 98155 44321")
                    .password(passwordEncoder.encode("Customer@123"))
                    .role(Role.CUSTOMER)
                    .build();

            userRepository.saveAll(List.of(customer1, customer2, customer3, customer4));
        }
    }

    private void seedVehicles() {
        if (vehicleRepository.count() == 0) {
            log.info("Seeding Initial Wedding & Celebration Vehicles...");
            Category hatchback = categoryRepository.findByNameIgnoreCase("Hatchback").orElseThrow();
            Category sedan = categoryRepository.findByNameIgnoreCase("Sedan").orElseThrow();
            Category suv = categoryRepository.findByNameIgnoreCase("SUV").orElseThrow();
            Category luxury = categoryRepository.findByNameIgnoreCase("Luxury").orElseThrow();
            Category electric = categoryRepository.findByNameIgnoreCase("Electric").orElseThrow();

            List<Vehicle> vehicles = List.of(
                    Vehicle.builder()
                            .name("Rolls-Royce Ghost Series II")
                            .brand("Rolls-Royce")
                            .model("Ghost Series II")
                            .category(luxury)
                            .registrationNumber("CH-01-RR-0001")
                            .description("The undisputed pinnacle of wedding royalty. Adorned with iconic Spirit of Ecstasy, hand-stitched leather lounge, starlight headliner, and whisper-silent V12 glide for grand groom entries and royal couple send-offs.")
                            .fuelType("Petrol")
                            .transmission("Automatic")
                            .seatingCapacity(5)
                            .modelYear(2024)
                            .pricePerDay(new BigDecimal("65000.00"))
                            .location("Chandigarh (Sector 17)")
                            .imageUrl("/images/vehicles/rolls-royce-ghost.jpg")
                            .available(true)
                            .build(),

                    Vehicle.builder()
                            .name("BMW 7 Series 740Li M Sport")
                            .brand("BMW")
                            .model("7 Series 740Li")
                            .category(sedan)
                            .registrationNumber("CH-01-WD-5510")
                            .description("Stately German flagship sedan featuring the groundbreaking 31.3-inch 8K BMW Theatre Screen, sky lounge panoramic glass roof, and executive reclining lounge seating for royal bride & groom reception arrivals.")
                            .fuelType("Petrol")
                            .transmission("Automatic")
                            .seatingCapacity(5)
                            .modelYear(2024)
                            .pricePerDay(new BigDecimal("25000.00"))
                            .location("Chandigarh (Sector 35)")
                            .imageUrl("/images/vehicles/bmw-7-series.jpg")
                            .available(true)
                            .build(),

                    Vehicle.builder()
                            .name("Mercedes-Benz S-Class S450 Maybach Trim")
                            .brand("Mercedes-Benz")
                            .model("S-Class S450")
                            .category(luxury)
                            .registrationNumber("PB-10-LU-3001")
                            .description("The gold standard of bridal send-off (Doli) and luxury hospitality. Featuring heated massaging executive rear seats, 64-color ambient mood lighting, Burmester 4D surround sound, and floral bonnet readiness.")
                            .fuelType("Petrol")
                            .transmission("Automatic")
                            .seatingCapacity(5)
                            .modelYear(2024)
                            .pricePerDay(new BigDecimal("28000.00"))
                            .location("Ludhiana (Civil Lines)")
                            .imageUrl("/images/vehicles/mercedes-s-class.jpg")
                            .available(true)
                            .build(),

                    Vehicle.builder()
                            .name("Audi A8L Quattro Matrix")
                            .brand("Audi")
                            .model("A8 L")
                            .category(sedan)
                            .registrationNumber("RJ-14-JP-6002")
                            .description("Ultra-luxurious long-wheelbase flagship limousine featuring predictive active air suspension, dynamic HD Matrix laser headlights, and VIP relaxation seats. Perfect for royal palace weddings across Jaipur and Rajasthan.")
                            .fuelType("Petrol")
                            .transmission("Automatic")
                            .seatingCapacity(5)
                            .modelYear(2024)
                            .pricePerDay(new BigDecimal("22000.00"))
                            .location("Jaipur (C-Scheme)")
                            .imageUrl("/images/vehicles/audi-a8l.jpg")
                            .available(true)
                            .build(),

                    Vehicle.builder()
                            .name("Toyota Fortuner Legender 4x4 (Black Edition)")
                            .brand("Toyota")
                            .model("Fortuner Legender")
                            .category(suv)
                            .registrationNumber("PB-02-AM-4491")
                            .description("The undisputed king of Punjabi Baraat convoys! Dominant road presence, 7-seater dual-tone leather cabin, high ground clearance, and robust power for grand celebration escorts and VIP family entourages.")
                            .fuelType("Diesel")
                            .transmission("Automatic")
                            .seatingCapacity(7)
                            .modelYear(2024)
                            .pricePerDay(new BigDecimal("8500.00"))
                            .location("Amritsar (Mall Road)")
                            .imageUrl("/images/vehicles/fortuner-legender.jpg")
                            .available(true)
                            .build(),

                    Vehicle.builder()
                            .name("Rolls-Royce Phantom VIII Royal Wedding Edition")
                            .brand("Rolls-Royce")
                            .model("Phantom VIII")
                            .category(luxury)
                            .registrationNumber("DL-01-RR-0007")
                            .description("The world’s most prestigious motorcar. Handcrafted English excellence, bespoke starlight ceiling with shooting stars, lamb wool carpets, and privacy suite. Reserved for the grandest royal weddings and celebrity entries.")
                            .fuelType("Petrol")
                            .transmission("Automatic")
                            .seatingCapacity(5)
                            .modelYear(2024)
                            .pricePerDay(new BigDecimal("95000.00"))
                            .location("Delhi NCR (Aerocity)")
                            .imageUrl("/images/vehicles/rolls-royce-phantom.jpg")
                            .available(true)
                            .build(),

                    Vehicle.builder()
                            .name("Land Rover Defender 110 X-Dynamic")
                            .brand("Land Rover")
                            .model("Defender 110")
                            .category(suv)
                            .registrationNumber("PB-65-DF-0001")
                            .description("The viral sensation of modern Punjabi weddings! Towering silhouette, Santorini Black finish, matrix LED lights, and massive convoy authority. The #1 demanded vehicle for energetic Baraat arrivals and groom escorts.")
                            .fuelType("Diesel")
                            .transmission("Automatic")
                            .seatingCapacity(7)
                            .modelYear(2024)
                            .pricePerDay(new BigDecimal("25000.00"))
                            .location("Chandigarh (Sector 10)")
                            .imageUrl("/images/vehicles/defender-110.jpg")
                            .available(true)
                            .build(),

                    Vehicle.builder()
                            .name("Range Rover Autobiography LWB")
                            .brand("Land Rover")
                            .model("Range Rover Autobiography")
                            .category(suv)
                            .registrationNumber("CH-01-RR-1111")
                            .description("The definitive aristocrat of luxury SUVs. Long wheelbase with executive class comfort-plus rear seats, active noise cancellation, and majestic road presence for couple entries and VIP family dignitaries.")
                            .fuelType("Petrol")
                            .transmission("Automatic")
                            .seatingCapacity(5)
                            .modelYear(2024)
                            .pricePerDay(new BigDecimal("45000.00"))
                            .location("Chandigarh (Sector 8)")
                            .imageUrl("/images/vehicles/range-rover.jpg")
                            .available(true)
                            .build(),

                    Vehicle.builder()
                            .name("Toyota Land Cruiser 300 ZX (LC300)")
                            .brand("Toyota")
                            .model("Land Cruiser LC300")
                            .category(suv)
                            .registrationNumber("PB-10-LC-0009")
                            .description("The ultimate symbol of power, influence, and high prestige across North India. Twin-turbo V6 engine, imposing front grille, and palatial interior comfort for Baraat convoy leaders and destination wedding royalty.")
                            .fuelType("Diesel")
                            .transmission("Automatic")
                            .seatingCapacity(7)
                            .modelYear(2024)
                            .pricePerDay(new BigDecimal("35000.00"))
                            .location("Ludhiana (Sarabha Nagar)")
                            .imageUrl("/images/vehicles/land-cruiser-300.png")
                            .available(true)
                            .build(),

                    Vehicle.builder()
                            .name("Mahindra Scorpio-N Z8L 4x4 (Baraat Escort Edition)")
                            .brand("Mahindra")
                            .model("Scorpio-N")
                            .category(suv)
                            .registrationNumber("PB-08-SN-0008")
                            .description("The beloved Big Daddy of SUVs. Equipped with Sony 3D immersive audio, 4x4 capability, captain seats, and sunroof. The top choice for spirited Baraat band-baja convoys and wedding coordination logistics.")
                            .fuelType("Diesel")
                            .transmission("Automatic")
                            .seatingCapacity(7)
                            .modelYear(2024)
                            .pricePerDay(new BigDecimal("7000.00"))
                            .location("Jalandhar (Model Town)")
                            .imageUrl("/images/vehicles/scorpio-n.jpg")
                            .available(true)
                            .build(),

                    Vehicle.builder()
                            .name("Porsche Taycan 4S Luxury EV")
                            .brand("Porsche")
                            .model("Taycan 4S")
                            .category(electric)
                            .registrationNumber("DL-01-EV-9110")
                            .description("Exotic electric performance masterpiece delivering breathtaking lines and silent zero-emission luxury. Unbeatable choice for futuristic pre-wedding shoots, cinematic reels, and elite reception arrivals.")
                            .fuelType("Electric")
                            .transmission("Automatic")
                            .seatingCapacity(4)
                            .modelYear(2024)
                            .pricePerDay(new BigDecimal("30000.00"))
                            .location("Delhi NCR (Gurugram)")
                            .imageUrl("/images/vehicles/porsche-taycan.jpg")
                            .available(true)
                            .build(),

                    Vehicle.builder()
                            .name("Mini Cooper S Convertible (Open-Top)")
                            .brand("Mini")
                            .model("Cooper S Convertible")
                            .category(hatchback)
                            .registrationNumber("CH-01-MC-3321")
                            .description("Iconic open-roof British convertible sports roadster. The viral sensation for bride & groom open-air entries, flower petal shower drives, and romantic celebration cinematography.")
                            .fuelType("Petrol")
                            .transmission("Automatic")
                            .seatingCapacity(4)
                            .modelYear(2024)
                            .pricePerDay(new BigDecimal("18000.00"))
                            .location("Chandigarh (Sector 35)")
                            .imageUrl("/images/vehicles/mini-cooper-convertible.jpg")
                            .available(true)
                            .build(),

                    Vehicle.builder()
                            .name("Mercedes-Benz G-Class G63 AMG (G-Wagon)")
                            .brand("Mercedes-Benz")
                            .model("G 63 AMG")
                            .category(luxury)
                            .registrationNumber("CH-01-GW-0063")
                            .description("Thunderous handcrafted 4.0L V8 twin-turbo icon with side-exit sports exhaust and unmistakable boxy silhouette. The supreme showstopper for grooms demanding an electrifying, jaw-dropping Baraat entry.")
                            .fuelType("Petrol")
                            .transmission("Automatic")
                            .seatingCapacity(5)
                            .modelYear(2024)
                            .pricePerDay(new BigDecimal("55000.00"))
                            .location("Chandigarh (Sector 9)")
                            .imageUrl("/images/vehicles/mercedes-g-wagon.jpg")
                            .available(true)
                            .build(),

                    Vehicle.builder()
                            .name("Mercedes-Benz E-Class LWB E350d")
                            .brand("Mercedes-Benz")
                            .model("E-Class LWB")
                            .category(sedan)
                            .registrationNumber("PB-10-MB-3500")
                            .description("Long-wheelbase executive sedan featuring first-class reclining rear lounge seats, touch controls, and silky smooth airmatic suspension. The gold-standard car for VIP family elders and wedding party transfers.")
                            .fuelType("Diesel")
                            .transmission("Automatic")
                            .seatingCapacity(5)
                            .modelYear(2024)
                            .pricePerDay(new BigDecimal("16000.00"))
                            .location("Ludhiana (Pakhowal Road)")
                            .imageUrl("/images/vehicles/mercedes-e-class.jpg")
                            .available(true)
                            .build()
            );

            vehicleRepository.saveAll(vehicles);
        }
    }

    private void seedBookings() {
        if (bookingRepository.count() == 0) {
            log.info("Seeding Initial Wedding Bookings...");
            User john = userRepository.findByEmail("gurpreet.singh@example.com").orElse(null);
            User sarah = userRepository.findByEmail("simran.kaur@example.com").orElse(null);
            List<Vehicle> allVehicles = vehicleRepository.findAll();

            if (john != null && sarah != null && allVehicles.size() >= 4) {
                Vehicle v1 = allVehicles.get(0); // Tesla Model 3
                Vehicle v2 = allVehicles.get(1); // BMW 530i
                Vehicle v3 = allVehicles.get(4); // Fortuner Legender

                // Sample confirmed booking (Groom Entry)
                Booking b1 = Booking.builder()
                        .bookingReference("VSB-100201-9214")
                        .user(john)
                        .vehicle(v1)
                        .pickupDate(LocalDate.now().plusDays(2))
                        .returnDate(LocalDate.now().plusDays(5))
                        .pickupLocation("Chandigarh (Sector 17 Resort)")
                        .numberOfDays(3)
                        .pricePerDay(v1.getPricePerDay())
                        .totalAmount(v1.getPricePerDay().multiply(BigDecimal.valueOf(3)))
                        .status(BookingStatus.CONFIRMED)
                        .notes("Groom arrival booking with floral decoration requested.")
                        .build();

                // Sample pending booking (Baraat Convoy)
                Booking b2 = Booking.builder()
                        .bookingReference("VSB-100202-4412")
                        .user(sarah)
                        .vehicle(v2)
                        .pickupDate(LocalDate.now().plusDays(7))
                        .returnDate(LocalDate.now().plusDays(10))
                        .pickupLocation("Delhi NCR (Aerocity Grand Hotel)")
                        .numberOfDays(3)
                        .pricePerDay(v2.getPricePerDay())
                        .totalAmount(v2.getPricePerDay().multiply(BigDecimal.valueOf(3)))
                        .status(BookingStatus.PENDING)
                        .notes("Bridal send-off (Doli) reservation awaiting review.")
                        .build();

                // Sample completed booking (Pre-wedding shoot)
                Booking b3 = Booking.builder()
                        .bookingReference("VSB-100199-8831")
                        .user(john)
                        .vehicle(v3)
                        .pickupDate(LocalDate.now().minusDays(10))
                        .returnDate(LocalDate.now().minusDays(6))
                        .pickupLocation("Jaipur (Palace Grounds)")
                        .numberOfDays(4)
                        .pricePerDay(v3.getPricePerDay())
                        .totalAmount(v3.getPricePerDay().multiply(BigDecimal.valueOf(4)))
                        .status(BookingStatus.COMPLETED)
                        .notes("Completed pre-wedding video shoot convoy without any damage.")
                        .build();

                bookingRepository.saveAll(List.of(b1, b2, b3));
            }
        }
    }
}
