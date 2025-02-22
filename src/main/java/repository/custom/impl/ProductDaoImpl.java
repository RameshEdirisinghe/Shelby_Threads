package repository.custom.impl;

import DBConnection.DBConnection;
import Entity.ProductEntity;
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
    public ProductEntity searchByName(String productName) throws SQLException {
        ResultSet rst = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT * FROM Product where ProductName='"+productName+"'");
        while (rst.next()) {
            return new ProductEntity(rst.getInt(1),rst.getString(2), rst.getString(3),rst.getString(4), rst.getDouble(5),rst.getInt(6), rst.getString(7), rst.getString(8) );
        }
        return null;
    }

    @Override
    public boolean save(ProductEntity entity) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try {

            ResultSet rst = connection.createStatement().executeQuery("Select SupplierID from Supplier where Name='" + entity.getSuppler() + "'");

            if (rst.next()) {
                PreparedStatement stm = connection.prepareStatement("Insert Into Product(ProductName,Category,Size,Price,Quantity,Image,SupplierID) values (?,?,?,?,?,?,?)");
                stm.setString(1, entity.getName());
                stm.setString(2, entity.getCategory());
                stm.setString(3, entity.getSize());
                stm.setDouble(4, entity.getPrice());
                stm.setInt(5, entity.getQty());
                stm.setString(6, entity.getImgPath());
                stm.setInt(7, rst.getInt(1));
                return stm.executeUpdate() > 0;
            }

            return false;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(Integer integer, ProductEntity entity) {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement stm = connection.prepareStatement("UPDATE product SET ProductName = ?, Category = ?, Size = ?,Price =?,Quantity =?,Image=? WHERE ProductID =?;");
            stm.setString(1, entity.getName());
            stm.setString(2, entity.getCategory());
            stm.setString(3, entity.getSize());
            stm.setString(4, entity.getPrice().toString());
            stm.setString(5, entity.getQty()+"");
            stm.setString(6, entity.getImgPath());
            stm.setInt(7, integer);

            return stm.executeUpdate()>0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(Integer integer) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement stm = connection.prepareStatement("DELETE FROM product WHERE productId=?");
        stm.setInt(1, integer);
        return stm.executeUpdate() > 0;
    }

    @Override
    public List<ProductEntity> getAll() throws SQLException {
        List<ProductEntity> productList = new ArrayList<>();
        ResultSet rst = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT * FROM Product");
        while (rst.next()) {
            productList.add(new ProductEntity(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getDouble(5), rst.getInt(6), rst.getString(7), rst.getString(8)));
        }
        return productList;
    }

    @Override
    public ProductEntity search(Integer integer) {
        try {
            String query = "SELECT * FROM product WHERE productId=?";
            PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement(query);
            stmt.setInt(1, integer); // Set empId parameter
            ResultSet rst = stmt.executeQuery();

            if (rst.next()) {
                String query1 = "SELECT * FROM supplier WHERE SupplierID=?";
                PreparedStatement stmtt = DBConnection.getInstance().getConnection().prepareStatement(query1);
                stmtt.setString(1, rst.getString(8)); // Set empId parameter
                ResultSet rstt = stmtt.executeQuery();

                if (rstt.next()) {
                    return new ProductEntity(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getDouble(5), rst.getInt(6), rst.getString(7), rstt.getString(2));
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
