package restaurant.dao.impl;


import restaurant.dao.MenuItemDAO;
import restaurant.model.Menu_items;
import restaurant.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MenuItemDAOImpl implements MenuItemDAO {
    @Override
    public boolean addMenuItem(Menu_items item) {
        String sql = "INSERT INTO menu_items (name, price, type, stock, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, item.getName());
            stmt.setDouble(2, item.getPrice());
            stmt.setString(3, item.getType());
            stmt.setInt(4, item.getStock());
            stmt.setString(5, item.getStatus());
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Lỗi thêm món: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updatePrice(int id, double newPrice) {
        // Thay vì xóa cứng (DELETE), ta nên xóa mềm bằng cách đổi status (best practice)
        String sql = "UPDATE menu_items SET status = 'OUT_OF_STOCK' WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteMenuItem(int id) {
        // Thay vì xóa cứng (DELETE), ta nên xóa mềm bằng cách đổi status (best practice)
        String sql = "UPDATE menu_items SET status = 'OUT_OF_STOCK' WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Menu_items> getAllMenuItems() {
        List<Menu_items> list = new ArrayList<>();
        String sql = "SELECT * FROM menu_items";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Menu_items item = new Menu_items();
                item.setId(rs.getInt("id"));
                item.setName(rs.getString("name"));
                item.setPrice(rs.getDouble("price"));
                item.setType(rs.getString("type"));
                item.setStock(rs.getInt("stock"));
                item.setStatus(rs.getString("status"));
                list.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

