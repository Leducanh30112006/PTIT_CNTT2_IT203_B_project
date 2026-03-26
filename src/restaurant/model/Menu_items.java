package restaurant.model;

public class Menu_items {
    private int id;
    private String name;
    private double price;
    private String type; // 'FOOD', 'DRINK'
    private int stock;
    private String status; // 'AVAILABLE', 'OUT_OF_STOCK'

    public Menu_items() {
    }

    public Menu_items(int id, String name, double price, String type, int stock, String status) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.type = type;
        this.stock = stock;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
