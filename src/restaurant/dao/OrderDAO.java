package restaurant.dao;



import restaurant.model.OrderDetail;
import java.util.List;

public interface OrderDAO {
    int createOrder(int userId, int tableId); // Tạo hóa đơn mới
    boolean addOrderDetail(int orderId, int itemId, int quantity); // Thêm món vào hóa đơn
    List<OrderDetail> getOrderDetailsByOrderId(int orderId); // Khách xem món đã gọi
    List<OrderDetail> getAllPendingDetails(); // Đầu bếp xem món đang chờ
    boolean updateStatus(int detailId, String status); // Cập nhật PENDING -> COOKING...
    boolean cancelDetail(int detailId); // Hủy món
    double printInvoiceAndGetTotal(int orderId);
    boolean checkoutOrder(int orderId);
}