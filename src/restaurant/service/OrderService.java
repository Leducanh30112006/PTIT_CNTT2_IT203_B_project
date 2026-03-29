package restaurant.service;

import restaurant.model.OrderDetail;
import java.util.List;

public interface OrderService {
    // Khách hàng chọn bàn và bắt đầu phiên gọi món
    int startOrder(int userId, int tableId);

    // Khách hàng thêm món vào đơn
    void placeOrder(int orderId, int itemId, int qty);

    // Khách hàng hủy món khi còn PENDING
    void cancelItem(int detailId);

    // Hiển thị danh sách món đã gọi (dạng bảng căn lề chuẩn)
    void displayOrderForCustomer(int orderId);

    // Đầu bếp xem tất cả các món đang chờ/nấu
    void displayAllPendingForChef();

    // Đầu bếp cập nhật trạng thái món
    void updateItemStatus(int detailId, String newStatus);
}