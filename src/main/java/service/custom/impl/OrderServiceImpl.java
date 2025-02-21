package service.custom.impl;

import model.Order;
import repository.DaoFactory;
import repository.custom.OrderDao;
import service.custom.OrderService;
import util.DaoType;

import java.sql.SQLException;

public class OrderServiceImpl implements OrderService {

    OrderDao orderDao = DaoFactory.getInstance().getDaoType(DaoType.ORDER);

    @Override
    public boolean placeOrder(Order order) throws SQLException {
        return  orderDao.save(order);

    }

    @Override
    public int getAllOrderCount() {
        return orderDao.getAllOrderCount();
    }
}
