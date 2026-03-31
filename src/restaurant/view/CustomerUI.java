package restaurant.view;

import restaurant.model.User;
import restaurant.service.OrderService;
import restaurant.service.TableService;
import restaurant.service.impl.OrderServiceImpl;
import restaurant.service.ManagerService;
import restaurant.util.InputValidator;
import java.util.Scanner;

public class CustomerUI {
    private final Scanner scanner;
    private final OrderService orderService;
    private final ManagerService managerService;
    private final TableService tableService;
    private int currentOrderId = -1;

    public CustomerUI(Scanner scanner) {
        this.scanner = scanner;
        this.orderService = new OrderServiceImpl();
        this.managerService = new ManagerService();
        this.tableService = new TableService(); // 2. Khởi tạo TableService
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
            System.out.println("4. Hủy gọi đồ");
            System.out.println("5. Tính tiền & Trả bàn"); // 3. Thêm Option Thanh toán
            System.out.println("0. Đăng xuất");
            System.out.print("Lựa chọn của bạn: ");

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
                        orderService.displayOrderForCustomer(currentOrderId);
                    }
                    break;
                case "4":
                    handleCancelItem();
                    break;
                case "5":

                    if (currentOrderId == -1) {
                        System.out.println("Bạn chưa ngồi bàn nào hoặc chưa gọi món!");
                    } else {
                        // Gọi luồng tính tiền
                        orderService.checkout(currentOrderId);

                        currentOrderId = -1;
                    }
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
            System.out.println("\n>>> DANH SÁCH BÀN TRỐNG:");
            //in ra các bàn đang FREE
            tableService.displayFreeTables();

            int tableId = InputValidator.readIntPositive("Nhập ID Bàn bạn muốn ngồi: ");


            // CHECK TRẠNG THÁI BÀN

            restaurant.model.Table selectedTable = tableService.getTableById(tableId);

            if (selectedTable == null) {
                System.out.println("Lỗi: Bàn có ID " + tableId + " không tồn tại trong hệ thống!");
                return;
            }

            if (!selectedTable.getStatus().equalsIgnoreCase("FREE")) {
                System.out.println("Lỗi: Bàn '" + selectedTable.getTableNumber() + "' đang có khách hoặc đã được đặt!");
                System.out.println("Vui lòng chọn một bàn khác đang ở trạng thái FREE.");
                return;
            }


            // 3. Vượt qua bài test rồi thì mới bắt đầu tạo Hóa đơn
            currentOrderId = orderService.startOrder(userId, tableId);

            if (currentOrderId != -1) {
                System.out.println("Đã nhận bàn thành công! Mã hóa đơn của bạn là: #" + currentOrderId);
            } else {
                System.out.println("Lỗi hệ thống khi tạo hóa đơn.");
                return;
            }
        }

        // Bàn thành công rồi thì hiện Menu món và cho gọi
        managerService.displayAllMenu();
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