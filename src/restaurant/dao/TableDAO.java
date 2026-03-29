package restaurant.dao;

import restaurant.model.Table;
import java.util.List;

public interface TableDAO {
    boolean addTable(Table table);
    boolean updateTable(Table table);
    boolean deleteTable(int id);
    Table getTableById(int id);
    List<Table> getAllTables();
}