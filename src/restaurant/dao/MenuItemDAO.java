package restaurant.dao;


import restaurant.model.Menu_items;
import java.util.List;

public interface MenuItemDAO {
    boolean addMenuItem(Menu_items item);
    boolean updatePrice(int id, double newPrice);
    boolean deleteMenuItem(int id);
    List<Menu_items> getAllMenuItems();
}