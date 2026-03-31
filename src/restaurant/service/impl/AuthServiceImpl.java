package restaurant.service.impl;

import restaurant.dao.UserDAO;
import restaurant.dao.impl.UserDAOImpl;
import restaurant.model.User;
import restaurant.service.AuthService;
import restaurant.util.PasswordHasher;

public class AuthServiceImpl implements AuthService {

    private final UserDAO userDAO = new UserDAOImpl();

    @Override
    public User login(String username, String rawPassword) {
        String hashedPassword = PasswordHasher.hash(rawPassword);

        User user = userDAO.getUserByUsernameAndPassword(username, hashedPassword);

        if (user != null) {
            System.out.println("Đăng nhập thành công! Chào mừng " + user.getFullName());
        } else {

            System.out.println("Lỗi: Sai tên đăng nhập hoặc mật khẩu. Vui lòng thử lại!");

        }
        return user;

    }

    @Override
    public boolean register(String username, String rawPassword, String fullName) {
        if (userDAO.isUsernameExists(username)) {
            System.out.println("Lỗi: Tên đăng nhập '" + username + "' đã tồn tại!");
            return false;
        }

        String hashedPassword = PasswordHasher.hash(rawPassword);

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(hashedPassword);
        newUser.setFullName(fullName);
        newUser.setRole("CUSTOMER");

        boolean isSuccess = userDAO.registerCustomer(newUser);
        if (isSuccess) {
            System.out.println("Đăng ký thành công!");
        } else {
            System.out.println("Lỗi hệ thống khi lưu tài khoản.");
        }
        return isSuccess;
    }
}