package restaurant.dao.impl;

import restaurant.dao.TableDAO;
import restaurant.model.Table;
import restaurant.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TableDAOImpl implements TableDAO {

    @Override
    public boolean addTable(Table table) {
        String sql = "INSERT INTO tables (table_number, capacity, status) VALUES (?, ?, 'FREE')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, table.getTableNumber());
            stmt.setInt(2, table.getCapacity());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi bàn đã tồn tại");
            return false;
        }
    }

    @Override
    public boolean updateTable(Table table) {
        // Cập nhật số bàn và sức chứa dựa trên ID
        String sql = "UPDATE tables SET table_number = ?, capacity = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, table.getTableNumber());
            stmt.setInt(2, table.getCapacity());
            stmt.setInt(3, table.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
//
            System.out.println("Lỗi bàn đã tồn tại");
            return false;
        }
    }

    @Override
    public boolean deleteTable(int id) {
        // Xóa bàn khỏi CSDL
        String sql = "DELETE FROM tables WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Table> getAllTables() {
        List<Table> list = new ArrayList<>();
        String sql = "SELECT * FROM tables";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Table(
                        rs.getInt("id"),
                        rs.getString("table_number"),
                        rs.getInt("capacity"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Table getTableById(int id) {
        String sql = "SELECT * FROM tables WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Table(
                        rs.getInt("id"),
                        rs.getString("table_number"),
                        rs.getInt("capacity"),
                        rs.getString("status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}