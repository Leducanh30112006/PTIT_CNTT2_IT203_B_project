CREATE DATABASE IF NOT EXISTS restaurant_manager;
USE restaurant_manager;

-- 1. Bảng người dùng
CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       full_name VARCHAR(100),
                       role ENUM('MANAGER', 'CHEF', 'CUSTOMER') NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Bảng bàn ăn
CREATE TABLE tables (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        table_number VARCHAR(10) NOT NULL UNIQUE,
                        capacity INT NOT NULL,
                        status ENUM('FREE', 'OCCUPIED', 'RESERVED') DEFAULT 'FREE'
);

-- 3. Bảng món ăn/đồ uống
CREATE TABLE menu_items (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(100) NOT NULL,
                            price DOUBLE NOT NULL,
                            type ENUM('FOOD', 'DRINK') NOT NULL,
                            stock INT DEFAULT 0,
                            status ENUM('AVAILABLE', 'OUT_OF_STOCK') DEFAULT 'AVAILABLE'
);

-- 4. Bảng hóa đơn tổng (Orders)
CREATE TABLE orders (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        user_id INT NOT NULL,
                        table_id INT NOT NULL,
                        total_amount DOUBLE DEFAULT 0,
                        status ENUM('PENDING', 'PAID', 'CANCELLED') DEFAULT 'PENDING',
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES users(id),
                        FOREIGN KEY (table_id) REFERENCES tables(id)
);

-- 5. Bảng chi tiết order
CREATE TABLE order_details (
                               id INT AUTO_INCREMENT PRIMARY KEY,
                               order_id INT NOT NULL,
                               item_id INT NOT NULL,
                               quantity INT NOT NULL,
                               status ENUM('PENDING', 'COOKING', 'READY', 'SERVED', 'CANCELLED') DEFAULT 'PENDING',
                               FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
                               FOREIGN KEY (item_id) REFERENCES menu_items(id)
);