-- VehicleSphere Seed Data
-- Default passwords:
-- Admin: Admin@123
-- Customers: Customer@123

INSERT INTO categories (name, description) VALUES
('Hatchback', 'Agile support cars for city wedding errands, bridal salon transfers, and logistics.'),
('Sedan', 'Elegant classic sedans ideal for bridal entries, couple departures, and guest hospitality.'),
('SUV', 'Commanding SUVs perfect for grand Baraat processions, VIP guests, and destination wedding convoys.'),
('Luxury', 'Ultra-premium executive cars for groom entry, bridal send-off, and high-profile wedding receptions.'),
('Electric', 'Cutting-edge luxury electric vehicles offering silent comfort for wedding shoots and modern celebrations.')
ON CONFLICT (name) DO UPDATE SET description = EXCLUDED.description;

-- Seed Punjabi Customers
-- Password hash for 'Customer@123'
-- $2a$10$wKz01b97P2OsqB5tVj8JceN1oGqKqgTj5N1tY6JzM6k2u0Wz2e.Gy
INSERT INTO users (full_name, email, phone_number, password, role) VALUES
('Gurpreet Singh', 'gurpreet.singh@example.com', '+91 98765 43210', '$2a$10$wKz01b97P2OsqB5tVj8JceN1oGqKqgTj5N1tY6JzM6k2u0Wz2e.Gy', 'CUSTOMER'),
('Simran Kaur', 'simran.kaur@example.com', '+91 98765 43211', '$2a$10$wKz01b97P2OsqB5tVj8JceN1oGqKqgTj5N1tY6JzM6k2u0Wz2e.Gy', 'CUSTOMER'),
('Jaswinder Singh', 'jaswinder.singh@example.com', '+91 98765 43212', '$2a$10$wKz01b97P2OsqB5tVj8JceN1oGqKqgTj5N1tY6JzM6k2u0Wz2e.Gy', 'CUSTOMER'),
('Harleen Kaur', 'harleen.kaur@example.com', '+91 98155 44321', '$2a$10$wKz01b97P2OsqB5tVj8JceN1oGqKqgTj5N1tY6JzM6k2u0Wz2e.Gy', 'CUSTOMER')
ON CONFLICT (email) DO UPDATE SET 
    full_name = EXCLUDED.full_name,
    phone_number = EXCLUDED.phone_number;
