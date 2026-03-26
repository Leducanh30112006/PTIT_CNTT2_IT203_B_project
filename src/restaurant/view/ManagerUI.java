package restaurant.view;



import restaurant.model.User;
import restaurant.service.ManagerService;
import java.util.Scanner;

public class ManagerUI {
    private final ManagerService managerService;
    private final Scanner scanner;

    public ManagerUI(Scanner scanner) {
        this.managerService = new ManagerService();
        this.scanner = scanner;
    }

    public void display(User manager) {
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("\n=========================================");
            System.out.println("      MENU QUẢN LÝ (Xin chào " + manager.getFullName() + ")");
            System.out.println("=========================================");
            System.out.println("1. Xem danh sách thực đơn");
            System.out.println("2. Thêm món ăn/đồ uống mới");
            System.out.println("3. Cập nhật giá món ăn");
            System.out.println("0. Đăng xuất");
            System.out.print("👉 Chọn chức năng: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    managerService.displayMenu();
                    break;
                case "2":
                    System.out.print("Nhập tên món: ");
                    String name = scanner.nextLine();

                    System.out.print("Nhập giá tiền: ");
                    double price = Double.parseDouble(scanner.nextLine()); // Bọc try-catch nếu cần an toàn hơn

                    System.out.print("Loại (FOOD/DRINK): ");
                    String type = scanner.nextLine().toUpperCase();

                    int stock = 0;
                    if (type.equals("DRINK")) {
                        System.out.print("Nhập số lượng tồn kho: ");
                        stock = Integer.parseInt(scanner.nextLine());
                    }

                    managerService.addNewDish(name, price, type, stock);
                    break;
                case "3":
                    System.out.print("Nhập ID món cần sửa giá: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    System.out.print("Nhập giá mới: ");
                    double newPrice = Double.parseDouble(scanner.nextLine());
                    managerService.updateDishPrice(id, newPrice);
                    break;
                case "0":
                    System.out.println("Đã đăng xuất khỏi tài khoản Quản lý.");
                    isRunning = false;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }
}