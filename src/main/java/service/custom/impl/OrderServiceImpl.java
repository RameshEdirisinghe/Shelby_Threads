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
    public boolean placeOrder(Order order){
        try {
            return  orderDao.save(order);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getAllOrderCount() {
        return orderDao.getAllOrderCount();
    }
}
