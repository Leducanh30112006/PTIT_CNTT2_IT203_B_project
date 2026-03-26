package restaurant;

import restaurant.model.User;
import restaurant.view.ManagerUI;
import restaurant.service.AuthService;
import restaurant.util.DBConnection;

import java.sql.Connection;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) {


        // 1. Kiểm tra Database trước khi chạy app
        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            System.out.println(" Lỗi: Không thể kết nối Database. Vui lòng kiểm tra lại cấu hình!");
            return;
        }

        // 2. Khởi tạo các đối tượng dùng chung
        Scanner scanner = new Scanner(System.in);
        AuthService authService = new AuthService();
        boolean isRunning = true;

        // 3. Vòng lặp Menu Chính
        while (isRunning) {
            System.out.println("\n=========================================");
            System.out.println("  PHẦN MỀM QUẢN LÝ NHÀ HÀNG ");
            System.out.println("=========================================");
            System.out.println("1. Đăng nhập hệ thống");
            System.out.println("2. Đăng ký tài khoản (Dành cho Khách hàng)");
            System.out.println("0. Thoát chương trình");
            System.out.print("Lựa chọn của bạn (0-2): ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleLogin(scanner, authService);
                    break;
                case "2":
                    handleRegister(scanner, authService);
                    break;
                case "0":
                    System.out.println("\nCảm ơn bạn đã sử dụng hệ thống. Hẹn gặp lại!");
                    isRunning = false;
                    break;
                default:
                    System.out.println("\nLựa chọn không hợp lệ. Vui lòng nhập từ 0 đến 2.");
            }
        }

        scanner.close();
    }

    // --- HÀM XỬ LÝ ĐĂNG NHẬP ---
    private static void handleLogin(Scanner scanner, AuthService authService) {
        System.out.println("\n--- ĐĂNG NHẬP ---");
        System.out.print("Tên đăng nhập: ");
        String username = scanner.nextLine();
        System.out.print("Mật khẩu: ");
        String password = scanner.nextLine();

        User loggedInUser = authService.login(username, password);

        if (loggedInUser != null) {
            System.out.println("\nĐăng nhập thành công! Xin chào " + loggedInUser.getFullName());

            // Điều hướng dựa trên Role của tài khoản
            switch (loggedInUser.getRole()) {
                case "MANAGER":
                    // Chuyển quyền điều khiển sang màn hình Quản lý
                    ManagerUI managerUI = new ManagerUI(scanner);
                    managerUI.display(loggedInUser);
                    break;
                case "CHEF":
                    System.out.println(">>> [Đang chuyển hướng đến Menu Bếp... Chức năng đang xây dựng]");
                    // ChefUI
                    break;
                case "CUSTOMER":
                    System.out.println(">>> [Đang chuyển hướng đến Menu Khách hàng... Chức năng đang xây dựng]");
                    // CustomerUI
                    break;
                default:
                    System.out.println("Lỗi: Không nhận diện được quyền của tài khoản này!");
            }
        } else {
            System.out.println("Sai tài khoản hoặc mật khẩu! Vui lòng thử lại.");
        }
    }

    // --- HÀM XỬ LÝ ĐĂNG KÝ ---
    private static void handleRegister(Scanner scanner, AuthService authService) {
        System.out.println("\n--- ĐĂNG KÝ TÀI KHOẢN KHÁCH HÀNG ---");
        System.out.print("Nhập họ và tên: ");
        String fullName = scanner.nextLine();

        System.out.print("Nhập tên đăng nhập: ");
        String username = scanner.nextLine();

        System.out.print("Nhập mật khẩu: ");
        String password = scanner.nextLine();

        // Validate cơ bản không cho nhập rỗng
        if (username.trim().isEmpty() || password.trim().isEmpty() || fullName.trim().isEmpty()) {
            System.out.println("Lỗi: Không được để trống thông tin!");
            return;
        }

        authService.register(username, password, fullName);
    }
}
