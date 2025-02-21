package repository.custom.impl;

import DBConnection.DBConnection;
import model.CartDetails;
import model.Order;
import model.Product;
import repository.custom.ProductDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDao {


    @Override
    public boolean updateStock(Order entity) {
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
    public boolean save(Product entity) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Integer integer, Product entity) {
        return false;
    }

    @Override
    public boolean delete(Integer integer) {
        return false;
    }

    @Override
    public List<Product> getAll() throws SQLException {
        List<Product> productList = new ArrayList<>();
        ResultSet rst = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT * FROM Product");
        while (rst.next()) {
            productList.add(new Product(rst.getInt(1),rst.getString(2), rst.getString(3),rst.getString(4), rst.getDouble(5),rst.getInt(6), rst.getString(7), rst.getString(8) ));
        }
        return productList;
    }
}
