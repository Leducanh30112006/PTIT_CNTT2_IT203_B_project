package restaurant.service.impl;

import restaurant.dao.OrderDAO;
import restaurant.dao.impl.OrderDAOImpl;
import restaurant.model.OrderDetail;
import restaurant.service.OrderService;
import java.util.List;

public class OrderServiceImpl implements OrderService {

    private final OrderDAO orderDAO = new OrderDAOImpl();

    @Override
    public int startOrder(int userId, int tableId) {
        return orderDAO.createOrder(userId, tableId);
    }

    @Override
    public void placeOrder(int orderId, int itemId, int qty) {
        if (orderDAO.addOrderDetail(orderId, itemId, qty)) {
            System.out.println("Đã gửi yêu cầu món xuống bếp!");
        } else {
            System.out.println("Lỗi gọi món (ID món không tồn tại hoặc lỗi kết nối).");
        }
    }

    @Override
    public void cancelItem(int detailId) {
        // Logic check trạng thái PENDING đã được xử lý an toàn trong DAO
        if (orderDAO.cancelDetail(detailId)) {
            System.out.println("Đã hủy món thành công.");
        } else {
            System.out.println("Không thể hủy (Món đã nấu/đã xong hoặc không tồn tại).");
        }
    }

    @Override
    public void displayOrderForCustomer(int orderId) {
        List<OrderDetail> details = orderDAO.getOrderDetailsByOrderId(orderId);
        printOrderTable(details, "HÓA ĐƠN CỦA BẠN");
    }

    @Override
    public void displayAllPendingForChef() {
        List<OrderDetail> details = orderDAO.getAllPendingDetails();
        printOrderTable(details, "DANH SÁCH BẾP ĐANG CHỜ");
    }

    @Override
    public void updateItemStatus(int detailId, String newStatus) {
        if (orderDAO.updateStatus(detailId, newStatus)) {
            System.out.println("Trạng thái món đã cập nhật thành: " + newStatus);
        } else {
            System.out.println("Cập nhật thất bại.");
        }
    }

    // Hàm hỗ trợ in bảng căn lề chuẩn (Clean Code Ngày 5)
    private void printOrderTable(List<OrderDetail> details, String title) {
        System.out.println("\n--- " + title + " ---");
        System.out.println("---------------------------------------------------------");
        System.out.printf("| %-5s | %-10s | %-5s | %-15s |\n", "ID CT", "ID Món", "SL", "Trạng Thái");
        System.out.println("---------------------------------------------------------");
        for (OrderDetail d : details) {
            System.out.printf("| %-5d | %-10d | %-5d | %-15s |\n",
                    d.getId(), d.getItemId(), d.getQuantity(), d.getStatus());
        }
        System.out.println("---------------------------------------------------------");
    }
}