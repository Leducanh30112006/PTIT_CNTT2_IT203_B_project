package restaurant.service;

import restaurant.model.OrderDetail;
import java.util.List;

public interface OrderService {
    int startOrder(int userId, int tableId);
    void placeOrder(int orderId, int itemId, int qty);
    void cancelItem(int detailId);
    void displayOrderForCustomer(int orderId);
    void displayAllPendingForChef();
    void updateItemStatus(int detailId, String newStatus);
    void checkout(int orderId);
}