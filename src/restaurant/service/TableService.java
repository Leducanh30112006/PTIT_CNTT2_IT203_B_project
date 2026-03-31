package restaurant.service;

import restaurant.dao.TableDAO;
import restaurant.dao.impl.TableDAOImpl;
import restaurant.model.Table;
import restaurant.util.InputValidator;
import java.util.List;
import java.util.Scanner;

public class TableService {
    private static final TableDAO tableDAO = new TableDAOImpl();


    public static void  displayAllTables() {
        List<Table> tables = tableDAO.getAllTables();
        System.out.println("\n---------------------------------------------------------");
        System.out.printf("| %-5s | %-12s | %-10s | %-12s |\n", "ID", "Số bàn", "Sức chứa", "Trạng thái");
        System.out.println("---------------------------------------------------------");
        for (Table t : tables) {
            System.out.printf("| %-5d | %-12s | %-10d | %-12s |\n",
                    t.getId(), t.getTableNumber(), t.getCapacity(), t.getStatus());
        }
        System.out.println("---------------------------------------------------------");
    }


    public void handleTableManager(Scanner scanner) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- QUẢN LÝ BÀN ĂN ---");
            System.out.println("1. Xem danh sách bàn");
            System.out.println("2. Thêm bàn mới");
            System.out.println("3. Sửa thông tin bàn");
            System.out.println("4. Xóa bàn (Xác nhận Y/N)");
            System.out.println("0. Quay lại");
            System.out.print("Lựa chọn: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    displayAllTables();
                    break;
                case "2":
                    addTable();
                    break;
                case "3":
                    updateTable();
                    break;
                case "4":
                    deleteTable(scanner);
                    break;
                case "0":
                    back = true;
                    break;
                default: System.out.println("Sai lựa chọn!");
            }
        }
    }

    private void addTable() {
        String num = InputValidator.readNonEmptyString("Nhập mã bàn (VD: T01): ");
        int cap = InputValidator.readIntPositive("Nhập sức chứa: ");
        Table t = new Table(0, num, cap, "FREE");
        if (tableDAO.addTable(t))
            System.out.println("Thêm thành công!");
    }

    private void updateTable() {
        int id = InputValidator.readIntPositive("Nhập ID bàn cần sửa: ");
        Table old = tableDAO.getTableById(id);
        if (old == null) {
            System.out.println("Không tìm thấy ID này!"); return; }

        System.out.println(">>> Dữ liệu cũ: Số bàn " + old.getTableNumber() + ", Sức chứa " + old.getCapacity());
        old.setTableNumber(InputValidator.readNonEmptyString("Nhập số bàn mới: "));
        old.setCapacity(InputValidator.readIntPositive("Nhập sức chứa mới: "));
        if (tableDAO.updateTable(old))
            System.out.println("Cập nhật thành công!");
    }

    private void deleteTable(Scanner scanner) {
        int id = InputValidator.readIntPositive("Nhập ID bàn muốn xóa: ");
        Table old = tableDAO.getTableById(id);

        if (old == null) {
            System.out.println("Không tồn tại bàn có ID: " + id);
            return;
        }

        System.out.print("Bạn có chắc muốn xóa bàn " + old.getTableNumber() + "? (Y/N): ");


        String choice = scanner.nextLine().trim().toUpperCase();

        if (choice.equals("Y")) {
            if (tableDAO.deleteTable(id)) {
                System.out.println("Đã xóa thành công!");
            } else {
                System.out.println("Xóa thất bại (có lỗi xảy ra)!");
            }
        } else if (choice.equals("N")) {
            System.out.println("Đã hủy thao tác xóa.");
        } else {
            System.out.println("Lựa chọn không hợp lệ! Vui lòng chỉ nhập Y hoặc N.");
        }
    }
    public void displayFreeTables() {
        List<Table> tables = tableDAO.getAllTables();
        boolean hasFree = false;
        System.out.println("\n---------------------------------------------------------");
        System.out.printf("| %-5s | %-12s | %-10s | %-12s |\n", "ID", "Số bàn", "Sức chứa", "Trạng thái");
        System.out.println("---------------------------------------------------------");
        for (Table t : tables) {
            if (t.getStatus().equalsIgnoreCase("FREE")) {
                System.out.printf("| %-5d | %-12s | %-10d | %-12s |\n",
                        t.getId(), t.getTableNumber(), t.getCapacity(), t.getStatus());
                hasFree = true;
            }
        }
        System.out.println("---------------------------------------------------------");
        if (!hasFree) {
            System.out.println("Hiện tại nhà hàng đã hết bàn trống!");
        }
    }

    //  Lấy thông tin 1 bàn cụ thể để UI kiểm tra
    public Table getTableById(int id) {
        return tableDAO.getTableById(id);
    }
}