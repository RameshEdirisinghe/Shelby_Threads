package repository.custom.impl;

import DBConnection.DBConnection;
import model.CartDetails;
import model.Order;
import repository.custom.ProductOrderDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ProductOrderDaoImpl implements ProductOrderDao {
    @Override
    public boolean save(Order entity) throws SQLException {
        for (CartDetails orderDetail : entity.getCartItems()) {

            String SQL = "INSERT INTO orderproduct(OrderId,ProductName,Quantity) VALUES (?,?,?)";
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, entity.getOrderId());
            preparedStatement.setString(2, orderDetail.getProductName());
            preparedStatement.setInt(3, orderDetail.getQty());

            boolean isAdd = preparedStatement.executeUpdate() > 0;
            if (!isAdd) {
                return false;
            }

        }
        return true;
    }

    @Override
    public boolean update(Integer integer, Order entity) {
        return false;
    }

    @Override
    public boolean delete(Integer integer) {
        return false;
    }

    @Override
    public List<Order> getAll() {
        return List.of();
    }

    @Override
    public Order search(Integer integer) {
        return null;
    }
}
