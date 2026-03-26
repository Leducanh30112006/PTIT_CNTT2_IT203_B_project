package restaurant.service;



import restaurant.dao.UserDAO;
import restaurant.dao.impl.UserDAOImpl;
import restaurant.model.User;
import restaurant.util.PasswordHasher;

/**
 * Xử lý các logic nghiệp vụ liên quan đến Xác thực người dùng (Đăng nhập, Đăng ký)
 */
public class AuthService {

    // Sử dụng Interface UserDAO để đảm bảo tính đa hình và lỏng lẻo (loose coupling)
    private final UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAOImpl();
    }

    /**
     * Chức năng Đăng nhập
     * @param username Tên đăng nhập
     * @param rawPassword Mật khẩu chưa mã hóa (người dùng nhập từ bàn phím)
     * @return Đối tượng User nếu thành công, null nếu thất bại
     */
    public User login(String username, String rawPassword) {
        // Băm mật khẩu người dùng nhập vào bằng SHA-256
        String hashedPassword = PasswordHasher.hash(rawPassword);

        // Gọi xuống tầng DAO để kiểm tra với Database
        return userDAO.getUserByUsernameAndPassword(username, hashedPassword);
    }

    /**
     * Chức năng Đăng ký tài khoản (Dành riêng cho Khách hàng)
     * @param username Tên đăng nhập muốn tạo
     * @param rawPassword Mật khẩu muốn tạo
     * @param fullName Họ và tên khách hàng
     * @return true nếu đăng ký thành công, false nếu thất bại
     */
    public boolean register(String username, String rawPassword, String fullName) {
        // 1. Kiểm tra xem tên đăng nhập đã bị người khác lấy chưa
        if (userDAO.isUsernameExists(username)) {
            System.out.println("❌ Lỗi: Tên đăng nhập '" + username + "' đã tồn tại! Vui lòng chọn tên khác.");
            return false;
        }

        // 2. Băm mật khẩu trước khi lưu xuống Database (Bảo mật)
        String hashedPassword = PasswordHasher.hash(rawPassword);

        // 3. Đóng gói dữ liệu vào thực thể User
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(hashedPassword);
        newUser.setFullName(fullName);
        newUser.setRole("CUSTOMER"); // Ép cứng role là CUSTOMER cho chức năng đăng ký ngoài màn hình chính

        // 4. Gọi DAO để thực hiện lệnh INSERT INTO
        boolean isSuccess = userDAO.registerCustomer(newUser);

        if (isSuccess) {
            System.out.println("Đăng ký thành công! Bạn có thể đăng nhập ngay bây giờ.");
        } else {
            System.out.println("Lỗi hệ thống: Không thể lưu tài khoản lúc này.");
        }

        return isSuccess;
    }
}