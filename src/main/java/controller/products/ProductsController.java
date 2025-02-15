package controller.products;


import DBConnection.DBConnection;
import javafx.scene.input.MouseEvent;
import model.Product;
import model.Supplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductsController {
    private static ProductsController instance;

    private ProductsController() {
    }

    public static ProductsController getInstance() {
        return instance == null ? instance = new ProductsController() : instance;
    }

    public boolean addProducts(Product product){
        Connection connection = DBConnection.getInstance().getConnection();
        try {

            ResultSet rst = connection.createStatement().executeQuery("Select SupplierID from Supplier where Name='"+product.getSuppler()+"'");

            if (rst.next()) {
                PreparedStatement stm = connection.prepareStatement("Insert Into Product(ProductName,Category,Size,Price,Quantity,Image,SupplierID) values (?,?,?,?,?,?,?)");
                stm.setString(1, product.getName());
                stm.setString(2, product.getCategory());
                stm.setString(3, product.getSize());
                stm.setDouble(4, product.getPrice());
                stm.setInt(5, product.getQty());
                stm.setString(6, product.getImgPath());
                stm.setInt(7, rst.getInt(1));
                return stm.executeUpdate()>0;
            }

            return false;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void click(MouseEvent mouseEvent) {
        
    }

    public List<Product> getAllProduct() {

        List<Product> productList = new ArrayList<>();
        try {
            ResultSet rst = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT * FROM Product");
            while (rst.next()) {
                productList.add(new Product(rst.getInt(1),rst.getString(2), rst.getString(3),rst.getString(4), rst.getDouble(5),rst.getInt(6), rst.getString(7), rst.getString(8) ));
            }
            return productList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Product getProduct(String productName) {

        try {
            ResultSet rst = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT * FROM Product where ProductName='"+productName+"'");
            while (rst.next()) {
                return new Product(rst.getInt(1),rst.getString(2), rst.getString(3),rst.getString(4), rst.getDouble(5),rst.getInt(6), rst.getString(7), rst.getString(8) );
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Product searchProduct(String productId) {
        try {
            String query = "SELECT * FROM product WHERE productId=?";
            PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement(query);
            stmt.setString(1, productId); // Set empId parameter
            ResultSet rst = stmt.executeQuery();

            if (rst.next()) {

                String query1 = "SELECT * FROM supplier WHERE SupplierID=?";
                PreparedStatement stmtt = DBConnection.getInstance().getConnection().prepareStatement(query1);
                stmtt.setString(1, rst.getString(8)); // Set empId parameter
                ResultSet rstt = stmtt.executeQuery();

                if (rstt.next()) {
                    return new Product(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getDouble(5), rst.getInt(6), rst.getString(7), rstt.getString(2));
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteProduct(String productId){
        Connection connection = DBConnection.getInstance().getConnection();
        try {

            PreparedStatement stm = connection.prepareStatement("DELETE FROM product WHERE productId=?");
            stm.setString(1, productId);

            return stm.executeUpdate()>0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean updateProduct(Product product) {
        Connection connection = DBConnection.getInstance().getConnection();
        try {


            PreparedStatement stm = connection.prepareStatement("UPDATE product SET Name = ?, Category = ?, Size = ?,Price =?,Quantity =?,Image=? WHERE ProductID =?;");
            stm.setString(1, product.getName());
            stm.setString(2, product.getCategory());
            stm.setString(3, product.getSize());
            stm.setString(4, product.getPrice().toString());
            stm.setString(5, product.getQty()+"");
            stm.setString(6, product.getImgPath());
            stm.setInt(7, product.getId());

            return stm.executeUpdate()>0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
