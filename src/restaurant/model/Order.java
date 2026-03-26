package restaurant.model;

import java.sql.Timestamp;

public class Order {
    private int id;
    private int userId;
    private int tableId;
    private double totalAmount;
    private String status; // 'PENDING', 'PAID', 'CANCELLED'
    private Timestamp createdAt;

    public Order() {
    }

    public Order(int id, int userId, int tableId, double totalAmount, String status, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.tableId = tableId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
