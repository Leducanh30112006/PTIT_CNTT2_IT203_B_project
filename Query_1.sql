USE restaurant_manager;

-- ========================================================
-- 1. DỮ LIỆU BẢNG: users
-- Mật khẩu mặc định cho tất cả tài khoản là: 123456
-- (Mã hash SHA-256 tương ứng: 8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92)
-- ========================================================
INSERT INTO users (username, password, full_name, role) VALUES
                                                            ('admin', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'Quản lý Đức Anh', 'MANAGER'),
                                                            ('chef_huy', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'Bếp trưởng Huy', 'CHEF'),
                                                            ('khach_vip', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'Khách hàng VIP 1', 'CUSTOMER'),
                                                            ('khach_vang', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'Khách hàng Vãng lai', 'CUSTOMER');

-- ========================================================
-- 2. DỮ LIỆU BẢNG: tables (Bàn ăn)
-- ========================================================
INSERT INTO tables (table_number, capacity, status) VALUES
                                                        ('T01', 2, 'FREE'),
                                                        ('T02', 4, 'OCCUPIED'),
                                                        ('T03', 4, 'FREE'),
                                                        ('T04', 6, 'RESERVED'),
                                                        ('VIP-1', 10, 'FREE');

-- ========================================================
-- 3. DỮ LIỆU BẢNG: menu_items (Thực đơn)
-- Stock (tồn kho) chỉ áp dụng cho đồ uống
-- ========================================================
INSERT INTO menu_items (name, price, type, stock, status) VALUES
                                                              ('Phở Bò Kobe', 150000, 'FOOD', 0, 'AVAILABLE'),
                                                              ('Cơm Rang Dưa Bò', 55000, 'FOOD', 0, 'AVAILABLE'),
                                                              ('Gà Rán Phần', 65000, 'FOOD', 0, 'AVAILABLE'),
                                                              ('Salad Cá Hồi', 85000, 'FOOD', 0, 'AVAILABLE'),
                                                              ('Trà Đào Cam Sả', 35000, 'DRINK', 50, 'AVAILABLE'),
                                                              ('Bia Heineken', 25000, 'DRINK', 120, 'AVAILABLE'),
                                                              ('Nước Suối', 10000, 'DRINK', 0, 'OUT_OF_STOCK');

-- ========================================================
-- 4. DỮ LIỆU BẢNG: orders (Hóa đơn tổng)
-- Hóa đơn 1: Đang ăn (PENDING) ở bàn T02
-- Hóa đơn 2: Đã thanh toán (PAID) ở bàn T01
-- ========================================================
INSERT INTO orders (user_id, table_id, total_amount, status) VALUES
                                                                 (3, 2, 220000, 'PENDING'),  -- user_id 3 (khach_vip) ngồi bàn 2 (T02)
                                                                 (4, 1, 150000, 'PAID');     -- user_id 4 (khach_vang) ngồi bàn 1 (T01)

-- ========================================================
-- 5. DỮ LIỆU BẢNG: order_details (Chi tiết gọi món)
-- Ràng buộc với order_id ở trên
-- ========================================================
-- Chi tiết của Hóa đơn 1 (Đang chờ phục vụ)
INSERT INTO order_details (order_id, item_id, quantity, status) VALUES
                                                                    (1, 1, 1, 'COOKING'),  -- 1 Phở Bò Kobe đang nấu
                                                                    (1, 5, 2, 'SERVED');   -- 2 Trà Đào Cam Sả đã mang ra bàn

-- Chi tiết của Hóa đơn 2 (Đã hoàn thành)
INSERT INTO order_details (order_id, item_id, quantity, status) VALUES
                                                                    (2, 2, 1, 'SERVED'),   -- 1 Cơm rang
                                                                    (2, 4, 1, 'SERVED'),   -- 1 Salad
                                                                    (2, 6, 1, 'SERVED');   -- 1 Bia