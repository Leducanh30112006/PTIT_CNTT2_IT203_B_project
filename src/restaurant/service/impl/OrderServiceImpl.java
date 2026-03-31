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

    @Override
    public void checkout(int orderId) {
        double total = orderDAO.printInvoiceAndGetTotal(orderId);

        if (total == 0) {
            System.out.println("Không thể thanh toán hóa đơn trống (0 VNĐ).");
            return;
        }

        // 2. Yêu cầu khách xác nhận thanh toán
        while (true) {
            System.out.print("Bạn có muốn tiến hành thanh toán và trả bàn không? (Y/N): ");

            java.util.Scanner sc = new java.util.Scanner(System.in);
            String confirm = sc.nextLine().trim().toUpperCase();

            if (confirm.equals("Y")) {
                if (orderDAO.checkoutOrder(orderId)) {
                    System.out.println("THANH TOÁN THÀNH CÔNG!");
                    System.out.println("Trạng thái bàn của bạn đã được chuyển về: FREE");
                    System.out.println("Cảm ơn Quý khách và hẹn gặp lại!");
                } else {
                    System.out.println("Lỗi hệ thống: Không thể hoàn tất thanh toán.");
                }
                break;
            } else if (confirm.equals("N")) {
                System.out.println("ℹĐã hủy thao tác thanh toán. Bạn có thể tiếp tục ngồi tại bàn.");
                break;
            } else {
                System.out.println("Vui lòng chỉ nhập Y hoặc N.");
            }
        }
    }


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