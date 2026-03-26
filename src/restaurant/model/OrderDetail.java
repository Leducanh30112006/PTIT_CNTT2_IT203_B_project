package restaurant.model;

public class OrderDetail {
    private int id;
    private int orderId;
    private int itemId;
    private int quantity;
    private String status;

    public OrderDetail() {
    }

    public OrderDetail(int id, int orderId, int itemId, int quantity, String status) {
        this.id = id;
        this.orderId = orderId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
