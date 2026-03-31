package restaurant.dao.impl;




import restaurant.dao.UserDAO;
import restaurant.model.User;
import restaurant.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAOImpl implements UserDAO {

    @Override
    public User getUserByUsernameAndPassword(String username, String hashedPassword) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setFullName(rs.getString("full_name"));
                user.setRole(rs.getString("role"));
                return user;
            }
        } catch (Exception e) {
            System.err.println("Lỗi truy vấn đăng nhập: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean isUsernameExists(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();


            return rs.next();

        } catch (Exception e) {
            System.err.println("Lỗi kiểm tra username: " + e.getMessage());
        }
        return true;
    }

    @Override
    public boolean registerCustomer(User user) {
        // Mặc định role khi đăng ký bên ngoài luôn là 'CUSTOMER'
        String sql = "INSERT INTO users (username, password, full_name, role) VALUES (?, ?, ?, 'CUSTOMER')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getFullName());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            System.err.println("Lỗi lưu tài khoản mới: " + e.getMessage());
        }
        return false;
    }
}