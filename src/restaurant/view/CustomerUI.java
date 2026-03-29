package restaurant.view;

import restaurant.model.User;
import restaurant.service.OrderService;
import restaurant.service.impl.OrderServiceImpl;
import restaurant.service.ManagerService; // Dùng lại hàm hiển thị menu có sẵn
import restaurant.util.InputValidator; // Dùng bộ lọc dữ liệu
import java.util.Scanner;

public class CustomerUI {
    private final Scanner scanner;
    private final OrderService orderService; // Đổi từ DAO sang Service
    private final ManagerService managerService;
    private int currentOrderId = -1;

    public CustomerUI(Scanner scanner) {
        this.scanner = scanner;
        this.orderService = new OrderServiceImpl(); // Khởi tạo Impl
        this.managerService = new ManagerService();
    }

    public void display(User customer) {
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("\n=========================================");
            System.out.println("   MENU KHÁCH HÀNG (Xin chào: " + customer.getFullName() + ")");
            System.out.println("=========================================");
            System.out.println("1. Xem danh sách thực đơn");
            System.out.println("2. Chọn bàn & Gọi món");
            System.out.println("3. Theo dõi món đã gọi");
            System.out.println("4. Hủy gọi đồ (Chỉ khi PENDING)");
            System.out.println("0. Đăng xuất");
            System.out.print("👉 Lựa chọn của bạn: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":

                    managerService.displayAllMenu();
                    break;
                case "2":
                    handleOrder(customer.getId());
                    break;
                case "3":
                    if (currentOrderId == -1) {
                        System.out.println("Bạn chưa ngồi bàn nào. Hãy chọn bàn và gọi món trước!");
                    } else {
                        // Gọi Service hiển thị cho đúng cấu trúc
                        orderService.displayOrderForCustomer(currentOrderId);
                    }
                    break;
                case "4":
                    handleCancelItem();
                    break;
                case "0":
                    System.out.println("Tạm biệt Quý khách!");
                    isRunning = false;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void handleOrder(int userId) {
        if (currentOrderId == -1) {
            // Dùng InputValidator để chống crash
            int tableId = InputValidator.readIntPositive("Nhập ID Bàn bạn muốn ngồi: ");
            currentOrderId = orderService.startOrder(userId, tableId);

            if (currentOrderId != -1) {
                System.out.println("Đã nhận bàn thành công! Bắt đầu gọi món.");
            } else {
                System.out.println("Lỗi: Bàn này không tồn tại hoặc đang có khách.");
                return;
            }
        }

        int itemId = InputValidator.readIntPositive("Nhập ID món ăn muốn gọi: ");
        int qty = InputValidator.readIntPositive("Nhập số lượng: ");

        orderService.placeOrder(currentOrderId, itemId, qty);
    }

    private void handleCancelItem() {
        if (currentOrderId == -1) {
            System.out.println("Bạn chưa ngồi bàn nào!");
            return;
        }
        orderService.displayOrderForCustomer(currentOrderId);
        int detailId = InputValidator.readIntPositive("Nhập [ID CT] món muốn hủy: ");

        orderService.cancelItem(detailId);
    }
}