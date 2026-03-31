package restaurant.dao.impl;

import restaurant.dao.OrderDAO; // QUAN TRỌNG: Phải import Interface
import restaurant.model.OrderDetail;
import restaurant.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class OrderDAOImpl implements OrderDAO {

    @Override
    public int createOrder(int userId, int tableId) {
        String insertOrder = "INSERT INTO orders (user_id, table_id, status) VALUES (?, ?, 'PENDING')";
        String updateTable = "UPDATE tables SET status = 'OCCUPIED' WHERE id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmtOrder = conn.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement stmtTable = conn.prepareStatement(updateTable)) {

                stmtOrder.setInt(1, userId);
                stmtOrder.setInt(2, tableId);
                stmtOrder.executeUpdate();

                ResultSet rs = stmtOrder.getGeneratedKeys();
                int orderId = -1;
                if (rs.next()) orderId = rs.getInt(1);

                stmtTable.setInt(1, tableId);
                stmtTable.executeUpdate();

                conn.commit();
                return orderId;
            } catch (SQLException ex) {
                conn.rollback();
                ex.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean addOrderDetail(int orderId, int itemId, int quantity) {
        String sql = "INSERT INTO order_details (order_id, item_id, quantity, status) VALUES (?, ?, ?, 'PENDING')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.setInt(2, itemId);
            stmt.setInt(3, quantity);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }


    @Override
    public List<OrderDetail> getOrderDetailsByOrderId(int orderId) {
        List<OrderDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM order_details WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new OrderDetail(
                        rs.getInt("id"),
                        rs.getInt("order_id"),
                        rs.getInt("item_id"),
                        rs.getInt("quantity"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Bổ sung hàm lấy tất cả món đang chờ cho Đầu bếp
    @Override
    public List<OrderDetail> getAllPendingDetails() {
        List<OrderDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM order_details WHERE status IN ('PENDING', 'COOKING', 'READY')";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new OrderDetail(
                        rs.getInt("id"), rs.getInt("order_id"),
                        rs.getInt("item_id"), rs.getInt("quantity"), rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean updateStatus(int detailId, String status) {
        String sql = "UPDATE order_details SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, detailId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean cancelDetail(int detailId) {
        // Chỉ cho phép xóa nếu món vẫn đang PENDING
        String sql = "DELETE FROM order_details WHERE id = ? AND status = 'PENDING'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, detailId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public double printInvoiceAndGetTotal(int orderId) {
        String sql = "SELECT m.name, m.price, od.quantity, (m.price * od.quantity) AS subtotal " +
                "FROM order_details od " +
                "JOIN menu_items m ON od.item_id = m.id " +
                "WHERE od.order_id = ? AND od.status != 'CANCELLED'";
        double total = 0.0;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\n=========================================================");
            System.out.println("                   HÓA ĐƠN THANH TOÁN                    ");
            System.out.println("=========================================================");
            System.out.printf("| %-20s | %-10s | %-4s | %-10s |\n", "Tên món", "Đơn giá", "SL", "Thành tiền");
            System.out.println("---------------------------------------------------------");

            boolean hasItems = false;
            while (rs.next()) {
                hasItems = true;
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int qty = rs.getInt("quantity");
                double subtotal = rs.getDouble("subtotal");
                total += subtotal;

                System.out.printf("| %-20s | %-10.0f | %-4d | %-10.0f |\n", name, price, qty, subtotal);
            }

            if (!hasItems) {
                System.out.println("| Hóa đơn trống (Chưa gọi món hoặc đã hủy hết)          |");
            }
            System.out.println("=========================================================");
            System.out.printf("  TỔNG CỘNG: %,.0f VNĐ\n", total);
            System.out.println("=========================================================");

        } catch (SQLException e) {
//            e.printStackTrace();
            System.out.println("Lỗi khi in hóa đơn: " + e.getMessage());
        }
        return total;
    }

    @Override
    public boolean checkoutOrder(int orderId) {
        String getTableId = "SELECT table_id FROM orders WHERE id = ?";
        String updateOrder = "UPDATE orders SET status = 'PAID' WHERE id = ?";
        String updateTable = "UPDATE tables SET status = 'FREE' WHERE id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Bật Transaction để đảm bảo an toàn dữ liệu

            try (PreparedStatement stmtSelect = conn.prepareStatement(getTableId);
                 PreparedStatement stmtOrder = conn.prepareStatement(updateOrder);
                 PreparedStatement stmtTable = conn.prepareStatement(updateTable)) {

                // Lấy ID bàn
                stmtSelect.setInt(1, orderId);
                ResultSet rs = stmtSelect.executeQuery();
                if (!rs.next()) return false;
                int tableId = rs.getInt("table_id");

                // Đổi Order -> PAID
                stmtOrder.setInt(1, orderId);
                stmtOrder.executeUpdate();

                // Đổi Table -> FREE
                stmtTable.setInt(1, tableId);
                stmtTable.executeUpdate();

                conn.commit(); // Thành công 100% thì mới lưu xuống MySQL
                return true;
            } catch (SQLException ex) {
                conn.rollback(); // Có lỗi thì hoàn tác ngay
                ex.printStackTrace();
            }
        } catch (SQLException e) {
//            e.printStackTrace();
            System.out.println("Lỗi khi thanh toán: " + e.getMessage());
        }
        return false;
    }

    // Giữ lại hàm in bảng để phục vụ UI nhanh
    public void displayOrderDetailsTable(int orderIdForCustomer) {
        String sql = "SELECT od.id, m.name, od.quantity, t.table_number, od.status " +
                "FROM order_details od " +
                "JOIN orders o ON od.order_id = o.id " +
                "JOIN menu_items m ON od.item_id = m.id " +
                "JOIN tables t ON o.table_id = t.id ";

        if (orderIdForCustomer != -1) {
            sql += "WHERE o.id = ? ";
        } else {
            sql += "WHERE od.status NOT IN ('SERVED', 'CANCELLED') ORDER BY od.id ASC";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (orderIdForCustomer != -1) {
                stmt.setInt(1, orderIdForCustomer);
            }

            ResultSet rs = stmt.executeQuery();
            System.out.println("\n-----------------------------------------------------------------");
            System.out.printf("| %-5s | %-20s | %-5s | %-10s | %-10s |\n", "ID CT", "Tên Món", "SL", "Bàn", "Trạng thái");
            System.out.println("-----------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("| %-5d | %-20s | %-5d | %-10s | %-10s |\n",
                        rs.getInt("id"), rs.getString("name"), rs.getInt("quantity"),
                        rs.getString("table_number"), rs.getString("status"));
            }
            System.out.println("-----------------------------------------------------------------");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}