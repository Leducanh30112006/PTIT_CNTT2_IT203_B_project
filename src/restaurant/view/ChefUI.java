package restaurant.view;

import restaurant.model.User;
import restaurant.service.OrderService;
import restaurant.service.impl.OrderServiceImpl;
import restaurant.util.InputValidator;
import java.util.Scanner;

public class ChefUI {
    private final Scanner scanner;
    private final OrderService orderService;

    public ChefUI(Scanner scanner) {
        this.scanner = scanner;
        this.orderService = new OrderServiceImpl();
    }

    public void display(User chef) {
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("\n=========================================");
            System.out.println("      MENU ĐẦU BẾP (Xin chào: " + chef.getFullName() + ")");
            System.out.println("=========================================");
            System.out.println("1. Danh sách món khách đang gọi");
            System.out.println("2. Cập nhật tiến độ nấu nướng");
            System.out.println("0. Đăng xuất");
            System.out.print("Lựa chọn của bạn: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":

                    orderService.displayAllPendingForChef();
                    break;
                case "2":
                    handleUpdateStatus();
                    break;
                case "0":
                    System.out.println("Đã đăng xuất khỏi tài khoản Đầu bếp.");
                    isRunning = false;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void handleUpdateStatus() {

        orderService.displayAllPendingForChef();


        int detailId = InputValidator.readIntPositive("Nhập [ID CT] món muốn cập nhật (hoặc 0 để quay lại): ");
        if (detailId == 0) return;

        System.out.println("\n--- CẬP NHẬT TRẠNG THÁI ---");
        System.out.println("1. COOKING (Đang nấu)");
        System.out.println("2. READY   (Đã xong, chờ phục vụ)");
        System.out.println("3. SERVED  (Đã phục vụ tận bàn)");
        System.out.println("0. Quay lại");
        System.out.print("Chọn trạng thái (0-3): ");

        String statusChoice = scanner.nextLine();
        String newStatus = "";

        switch (statusChoice) {
            case "1": newStatus = "COOKING"; break;
            case "2": newStatus = "READY"; break;
            case "3": newStatus = "SERVED"; break;
            case "0": return;
            default:
                System.out.println("Lựa chọn trạng thái không hợp lệ!");
                return;
        }


        orderService.updateItemStatus(detailId, newStatus);
    }
}