package restaurant;

import restaurant.model.User;
import restaurant.view.ChefUI;
import restaurant.view.CustomerUI;
import restaurant.view.ManagerUI;
import restaurant.service.AuthService;
import restaurant.service.impl.AuthServiceImpl;
import restaurant.util.DBConnection;

import java.sql.Connection;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) {


        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            System.out.println("Lỗi: Không thể kết nối Database. Vui lòng kiểm tra lại cấu hình!");
            return;
        }


        Scanner scanner = new Scanner(System.in);


        AuthService authService = new AuthServiceImpl();

        boolean isRunning = true;

        // 3. Vòng lặp Menu Chính
        while (isRunning) {
            System.out.println("\n=========================================");
            System.out.println("      HỆ THỐNG QUẢN LÝ NHÀ HÀNG          ");
            System.out.println("=========================================");
            System.out.println("1. Đăng nhập hệ thống");
            System.out.println("2. Đăng ký tài khoản (Khách hàng)");
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
                    System.out.println("\nLựa chọn không hợp lệ.");
            }
        }
        scanner.close();
    }

    private static void handleLogin(Scanner scanner, AuthService authService) {
        System.out.println("\n--- ĐĂNG NHẬP ---");
        System.out.print("Tên đăng nhập: ");
        String username = scanner.nextLine();
        System.out.print("Mật khẩu: ");
        String password = scanner.nextLine();

        User loggedInUser = authService.login(username, password);

        if (loggedInUser != null) {


            switch (loggedInUser.getRole()) {
                case "MANAGER":
                    new ManagerUI(scanner).display(loggedInUser);
                    break;
                case "CHEF":
                    new ChefUI(scanner).display(loggedInUser);
                    break;
                case "CUSTOMER":
                    new CustomerUI(scanner).display(loggedInUser);
                    break;
                default:
                    System.out.println("Lỗi: Quyền truy cập không hợp lệ!");
            }
        }
    }

    private static void handleRegister(Scanner scanner, AuthService authService) {
        System.out.println("\n---ĐĂNG KÝ KHÁCH HÀNG ---");
        System.out.print("Họ và tên: ");
        String fullName = scanner.nextLine();
        System.out.print("Tên đăng nhập: ");
        String username = scanner.nextLine();
        System.out.print("Mật khẩu: ");
        String password = scanner.nextLine();

        if (username.trim().isEmpty() || password.trim().isEmpty() || fullName.trim().isEmpty()) {
            System.out.println("Lỗi: Thông tin không được để rỗng!");
            return;
        }

        // Gọi hàm register trong interface
        authService.register(username, password, fullName);
    }
}