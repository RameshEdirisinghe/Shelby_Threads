package service.custom;

import model.Order;

import java.sql.SQLException;

public interface OrderService {
    boolean placeOrder(Order order) throws SQLException;

    int getAllOrderCount();
}
