package restaurant.service;

import restaurant.dao.MenuItemDAO;
import restaurant.dao.impl.MenuItemDAOImpl;
import restaurant.model.Menu_items;
import java.util.List;
import java.util.Scanner;

public class ManagerService {
    private final MenuItemDAO menuItemDAO;

    public ManagerService() {
        this.menuItemDAO = new MenuItemDAOImpl();
    }

    // --- 1. HIỂN THỊ DANH SÁCH ---
    public void displayMenu(List<Menu_items> menu) {
        if (menu == null || menu.isEmpty()) {
            System.out.println("Không có món ăn nào trong danh sách!");
            return;
        }
        System.out.println("\n-------------------------------------------------------------------------");
        System.out.printf("| %-5s | %-25s | %-12s | %-10s | %-5s |\n", "ID", "Tên món", "Giá (VNĐ)", "Loại", "Kho");
        System.out.println("-------------------------------------------------------------------------");
        for (Menu_items item : menu) {
            System.out.printf("| %-5d | %-25s | %-12.0f | %-10s | %-5d |\n",
                    item.getId(), item.getName(), item.getPrice(), item.getType(), item.getStock());
        }
        System.out.println("-------------------------------------------------------------------------");
    }

    public void displayAllMenu() {
        displayMenu(menuItemDAO.getAllMenuItems());
    }

    // --- 2. THÊM MÓN ---
    public void addNewDish(String name, double price, String type, int stock) {
        if (name == null || name.trim().length() < 2) {
            System.out.println("Lỗi: Tên món không hợp lệ (phải từ 2 ký tự trở lên)!");
            return;
        }
        if (price <= 0 || stock < 0) {
            System.out.println("Lỗi: Giá tiền phải > 0 và số lượng không được âm!");
            return;
        }

        Menu_items item = new Menu_items();
        item.setName(name);
        item.setPrice(price);
        item.setType(type);
        item.setStock(type.equals("DRINK") ? stock : 0);
        item.setStatus("AVAILABLE");

        if (menuItemDAO.addMenuItem(item)) {
            System.out.println("Thêm món '" + name + "' thành công!");
        } else {
            System.out.println("Thêm món thất bại do lỗi hệ thống.");
        }
    }

    // --- 3. SỬA MÓN ---
    public void updateDish(Scanner scanner) {
        System.out.print("Nhập ID món cần sửa: ");
        int id = Integer.parseInt(scanner.nextLine());

        Menu_items currentItem = menuItemDAO.getMenuItemById(id);
        if (currentItem == null) {
            System.out.println("Lỗi: Không tìm thấy món ăn có ID là " + id);
            return;
        }

        // Hiển thị thông tin hiện tại
        System.out.println(">>> Thông tin hiện tại: " + currentItem.getName() + " - Giá: " + currentItem.getPrice());

        System.out.print("Nhập giá mới (VNĐ): ");
        double newPrice = Double.parseDouble(scanner.nextLine());

        if (newPrice <= 0) {
            System.out.println("Lỗi: Giá tiền vô lý!");
            return;
        }

        if (menuItemDAO.updatePrice(id, newPrice)) {
            System.out.println("Cập nhật giá thành công!");
        } else {
            System.out.println("Cập nhật thất bại.");
        }
    }

    // --- 4. XÓA MÓN (Có xác nhận Y/N) ---
    public void deleteDish(Scanner scanner) {
        System.out.print("Nhập ID món cần xóa: ");
        int id = Integer.parseInt(scanner.nextLine());

        Menu_items currentItem = menuItemDAO.getMenuItemById(id);
        if (currentItem == null) {
            System.out.println("Lỗi: Không tồn tại món ăn với ID " + id);
            return;
        }

        System.out.print("Bạn có chắc chắn muốn xóa món '" + currentItem.getName() + "'? (Y/N): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("Y")) {
            if (menuItemDAO.deleteMenuItem(id)) {
                System.out.println("Xóa món thành công!");
            } else {
                System.out.println("Lỗi hệ thống khi xóa món.");
            }
        } else {
            System.out.println("Đã hủy thao tác xóa.");
        }
    }

    // --- 5. TÌM KIẾM THEO TÊN ---
    public void searchDishByName(Scanner scanner) {
        System.out.print("Nhập tên món cần tìm: ");
        String keyword = scanner.nextLine();

        List<Menu_items> results = menuItemDAO.searchMenuItemsByName(keyword);
        if (results.isEmpty()) {
            System.out.println("Không tìm thấy món nào khớp với từ khóa: " + keyword);
        } else {
            System.out.println("\n>>> KẾT QUẢ TÌM KIẾM:");
            displayMenu(results);
        }
    }
}