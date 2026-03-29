package restaurant.view;

import restaurant.model.User;
import restaurant.service.ManagerService;
import restaurant.service.TableService;
import restaurant.util.InputValidator;

import java.util.Scanner;

public class ManagerUI {
    private final ManagerService managerService;
    private final Scanner scanner;
    private final TableService tableService;

    public ManagerUI(Scanner scanner) {
        this.managerService = new ManagerService();
        this.scanner = scanner;
        this.tableService = new TableService();
    }

    public void display(User manager) {
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("\n=========================================");
            System.out.println("     HỆ THỐNG QUẢN LÝ THỰC ĐƠN / BÀN ĂN  ");
            System.out.println("           (Xin chào: " + manager.getFullName() + ")");
            System.out.println("=========================================");
            System.out.println("1. Hiển thị danh sách món ăn");
            System.out.println("2. Thêm món mới");
            System.out.println("3. Sửa thông tin món");
            System.out.println("4. Xóa món ăn");
            System.out.println("5. Tìm kiếm món theo tên");
            System.out.println("6. Quản lý bàn ăn ");
            System.out.println("0. Đăng xuất");
            System.out.print("Chọn chức năng (0-6): ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        managerService.displayAllMenu();
                        break;
                    case "2":
                        System.out.println("\n--- THÊM MÓN MỚI ---");
                        String name = InputValidator.readNonEmptyString("Nhập tên món: ");
//                        String name = scanner.nextLine();
                        double price = InputValidator.readDoublePositive("Nhập giá tiền: ");
//                        double price = Double.parseDouble(scanner.nextLine());
                        System.out.print("Phân loại (FOOD / DRINK): ");
                        String type = scanner.nextLine().toUpperCase();

                        int stock = 0;
                        if (type.equals("DRINK")) {
                            System.out.print("Nhập số lượng tồn kho: ");
                            stock = Integer.parseInt(scanner.nextLine());
                        }
                        managerService.addNewDish(name, price, type, stock);
                        break;
                    case "3":
                        System.out.println("\n--- SỬA THÔNG TIN MÓN ---");
                        managerService.updateDish(scanner);
                        break;
                    case "4":
                        System.out.println("\n--- XÓA MÓN ĂN ---");
                        managerService.deleteDish(scanner);
                        break;
                    case "5":
                        System.out.println("\n--- TÌM KIẾM MÓN ĂN ---");
                        managerService.searchDishByName(scanner);
                        break;
                    case "6":
                        tableService.handleTableManager(scanner);
                        break;

                    case "0":
                        System.out.println("Đã đăng xuất khỏi tài khoản Quản lý.");
                        isRunning = false;
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Lỗi: Bạn phải nhập số cho ID, Giá tiền hoặc Số lượng!");
            } catch (Exception e) {
                System.out.println("Lỗi hệ thống: " + e.getMessage());
            }
        }
    }
}