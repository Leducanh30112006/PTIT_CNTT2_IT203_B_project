package restaurant.service;



import restaurant.dao.MenuItemDAO;
import restaurant.dao.impl.MenuItemDAOImpl;
import restaurant.model.Menu_items;
import java.util.List;

public class ManagerService {
    private final MenuItemDAO menuItemDAO;

    public ManagerService() {
        this.menuItemDAO = new MenuItemDAOImpl();
    }

    // --- QUẢN LÝ THỰC ĐƠN ---

    public boolean addNewDish(String name, double price, String type, int stock) {
        // Validation: Giá và số lượng phải lớn hơn 0
        if (price <= 0) {
            System.out.println("Lỗi: Giá tiền phải lớn hơn 0!");
            return false;
        }
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Lỗi: Tên món không được để trống!");
            return false;
        }

        Menu_items item = new Menu_items();
        item.setName(name);
        item.setPrice(price);
        item.setType(type); // 'FOOD' hoặc 'DRINK'
        item.setStock(type.equals("DRINK") ? stock : 0);
        item.setStatus("AVAILABLE");

        boolean success = menuItemDAO.addMenuItem(item);
        if (success) System.out.println("Thêm món mới thành công!");
        return success;
    }

    public void displayMenu() {
        List<Menu_items> menu = menuItemDAO.getAllMenuItems();
        System.out.println("\n--- DANH SÁCH THỰC ĐƠN ---");
        System.out.printf("%-5s | %-20s | %-10s | %-10s | %-10s\n", "ID", "Tên món", "Giá", "Loại", "Trạng thái");
        System.out.println("---------------------------------------------------------------");
        for (Menu_items item : menu) {
            System.out.printf("%-5d | %-20s | %-10.0f | %-10s | %-10s\n",
                    item.getId(), item.getName(), item.getPrice(), item.getType(), item.getStatus());
        }
    }

    public void updateDishPrice(int id, double newPrice) {
        if (newPrice <= 0) {
            System.out.println("Lỗi: Giá mới phải lớn hơn 0!");
            return;
        }
        if (menuItemDAO.updatePrice(id, newPrice)) {
            System.out.println("Cập nhật giá thành công!");
        } else {
            System.out.println("Cập nhật thất bại. Kiểm tra lại ID món ăn.");
        }
    }
}