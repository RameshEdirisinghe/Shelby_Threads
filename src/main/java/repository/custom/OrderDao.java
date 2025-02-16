package repository.custom;


import model.Order;
import repository.CrudDao;

import java.sql.SQLException;


public interface OrderDao extends CrudDao<Order,Integer> {

    String getOrderId() throws SQLException;

    int getAllOrderCount();
}
