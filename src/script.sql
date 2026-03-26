-- 1. Tạo Database
CREATE DATABASE IF NOT EXISTS restaurant_db;
USE restaurant_db;

-- 2. Xóa bảng cũ (nếu có) để tránh xung đột khi chạy lại script
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS order_details;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS dining_tables;
DROP TABLE IF EXISTS dishes;
DROP TABLE IF EXISTS users;
SET FOREIGN_KEY_CHECKS = 1;

-- 3. Bảng Người dùng (Yêu cầu 2.0 & 3.1)
CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL, -- Sẽ lưu hash SHA-256
                       full_name VARCHAR(100),
                       role ENUM('MANAGER', 'CHEF', 'CUSTOMER') NOT NULL,
                       status ENUM('ACTIVE', 'BANNED') DEFAULT 'ACTIVE', -- Phục vụ tính năng nâng cao 2
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. Bảng Thực đơn (Yêu cầu 3.2)
CREATE TABLE dishes (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        price DOUBLE NOT NULL CHECK (price > 0), -- Ràng buộc giá phải dương (Yêu cầu 6.1)
                        category ENUM('FOOD', 'DRINK') NOT NULL,
                        stock INT DEFAULT 0, -- Số lượng tồn kho cho đồ uống
                        status ENUM('AVAILABLE', 'OUT_OF_STOCK') DEFAULT 'AVAILABLE'
);

-- 5. Bảng Bàn ăn (Yêu cầu 3.2)
CREATE TABLE dining_tables (
                               id INT AUTO_INCREMENT PRIMARY KEY,
                               table_number VARCHAR(10) NOT NULL UNIQUE,
                               capacity INT NOT NULL CHECK (capacity > 0),
                               status ENUM('FREE', 'OCCUPIED', 'RESERVED') DEFAULT 'FREE'
);

-- 6. Bảng Đơn hàng - Hóa đơn (Yêu cầu 3.3 & Nâng cao 1)
CREATE TABLE orders (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        user_id INT NOT NULL, -- Khách hàng nào đặt
                        table_id INT NOT NULL, -- Ngồi bàn nào
                        total_amount DOUBLE DEFAULT 0,
                        status ENUM('UNPAID', 'PAID', 'CANCELLED') DEFAULT 'UNPAID',
                        order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES users(id),
                        FOREIGN KEY (table_id) REFERENCES dining_tables(id)
);

-- 7. Chi tiết đơn hàng - Từng món ăn (Yêu cầu 3.4)
CREATE TABLE order_details (
                               id INT AUTO_INCREMENT PRIMARY KEY,
                               order_id INT NOT NULL,
                               dish_id INT NOT NULL,
                               quantity INT NOT NULL CHECK (quantity > 0),
    -- Trạng thái món ăn theo luồng yêu cầu
                               status ENUM('PENDING', 'COOKING', 'READY', 'SERVED', 'CANCELLED') DEFAULT 'PENDING',
                               note TEXT,
                               FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
                               FOREIGN KEY (dish_id) REFERENCES dishes(id)
);

-- ---------------------------------------------------------
-- DỮ LIỆU MẪU (Dùng để test hệ thống)
-- ---------------------------------------------------------

-- Tài khoản mẫu (Mật khẩu giả định đã hash hoặc để tạm 123456)
INSERT INTO users (username, password, full_name, role) VALUES
                                                            ('admin', 'admin123', 'Manager Đức Anh', 'MANAGER'),
                                                            ('chef01', 'chef123', 'Đầu bếp Huy', 'CHEF'),
                                                            ('customer01', 'customer123', 'Khách hàng A', 'CUSTOMER');

-- Thực đơn mẫu
INSERT INTO dishes (name, price, category, stock) VALUES
                                                      ('Phở Bò', 50000, 'FOOD', 0),
                                                      ('Bún Chả', 45000, 'FOOD', 0),
                                                      ('Cơm Tấm', 40000, 'FOOD', 0),
                                                      ('Coca Cola', 15000, 'DRINK', 50),
                                                      ('Trà Đá', 5000, 'DRINK', 100);

-- Bàn ăn mẫu
INSERT INTO dining_tables (table_number, capacity) VALUES
                                                       ('Bàn 01', 2),
                                                       ('Bàn 02', 4),
                                                       ('Bàn 03', 4),
                                                       ('Bàn 04', 6),
                                                       ('Bàn 05', 10);

-- Một ví dụ về đơn hàng đang chờ
INSERT INTO orders (user_id, table_id, total_amount, status) VALUES (3, 1, 65000, 'UNPAID');
INSERT INTO order_details (order_id, dish_id, quantity, status) VALUES
                                                                    (1, 1, 1, 'COOKING'), -- 1 Phở Bò đang nấu
                                                                    (1, 4, 1, 'SERVED');  -- 1 Coca đã phục vụ