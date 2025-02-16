package repository.custom.impl;

import DBConnection.DBConnection;
import model.CartDetails;
import model.Order;
import repository.custom.ProductDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ProductDaoImpl implements ProductDao {
    @Override
    public boolean save(Order entity) throws SQLException {
        for (CartDetails orderDetail : entity.getCartItems()) {
            String SQL = "UPDATE product SET Quantity=Quantity-? WHERE ProductName=?";
            try {
                Connection connection = DBConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL);
                statement.setInt(1, orderDetail.getQty());
                statement.setString(2, orderDetail.getProductName());
                boolean isItemUpdate = statement.executeUpdate() > 0;
                if (!isItemUpdate) {
                    return false;
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
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
}
