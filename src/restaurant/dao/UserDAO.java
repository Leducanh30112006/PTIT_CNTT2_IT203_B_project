package restaurant.dao;


import restaurant.model.User;

public interface UserDAO {
    // Dùng cho Đăng nhập
    User getUserByUsernameAndPassword(String username, String hashedPassword);

    // Dùng cho Đăng ký (Kiểm tra xem tên đăng nhập đã có ai tạo chưa)
    boolean isUsernameExists(String username);

    // Dùng cho Đăng ký (Lưu thông tin khách hàng mới)
    boolean registerCustomer(User user);
}