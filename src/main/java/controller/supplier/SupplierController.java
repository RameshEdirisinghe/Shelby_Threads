package controller.supplier;


import DBConnection.DBConnection;
import model.Supplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierController {
    private static SupplierController instance;

    private SupplierController() {
    }

    public static SupplierController getInstance() {
        return instance == null ? instance = new SupplierController() : instance;
    }

    public List<String> getAllSupplierNames(){
        List<String> supplierList = new ArrayList<>();

        try {
            ResultSet rst = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT name FROM Supplier");
            while (rst.next()) {

                supplierList.add(rst.getString(1));

            }
            return supplierList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Supplier> getAllSuppliers() {
        List<Supplier> supplierList = new ArrayList<>();

        try {
            ResultSet rst = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT * FROM supplier");
            while (rst.next()) {

                supplierList.add(new Supplier(rst.getInt(1), rst.getString(2), rst.getString(4), rst.getString(3),rst.getString(5)));
                System.out.println(supplierList);


            }
            return supplierList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public Supplier searchSupplier(String supplierId) {
        try {
            String query = "SELECT * FROM supplier WHERE SupplierID=?";
            PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement(query);
            stmt.setString(1, supplierId); // Set empId parameter
            ResultSet rst = stmt.executeQuery();

            if (rst.next()) {



                return new Supplier(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getString(4),rst.getString(5));

            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean addSupplier(Supplier supplier){
        Connection connection = DBConnection.getInstance().getConnection();
        try {

            PreparedStatement stm = connection.prepareStatement("Insert Into Supplier(Name,Email,company,Item) values (?,?,?,?)");
            stm.setString(1,supplier.getName());
            stm.setString(2,supplier.getEmail());
            stm.setString(3,supplier.getCompany());
            stm.setString(4,supplier.getItem());

            return stm.executeUpdate()>0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public boolean updateSupplier(Supplier supplier) {
        Connection connection = DBConnection.getInstance().getConnection();
        try {


            PreparedStatement stm = connection.prepareStatement("UPDATE supplier SET Name = ?, Email = ?, company = ?,Item =? WHERE SupplierID =?;");
            stm.setString(1, supplier.getName());
            stm.setString(2, supplier.getEmail());
            stm.setString(3, supplier.getCompany());
            stm.setString(4, supplier.getItem());
            stm.setInt(5, supplier.getId());

            return stm.executeUpdate()>0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public boolean deleteSupplier(String supplierId){
        Connection connection = DBConnection.getInstance().getConnection();
        try {

            PreparedStatement stm = connection.prepareStatement("DELETE FROM supplier WHERE SupplierID=?");
            stm.setString(1, supplierId);

            return stm.executeUpdate()>0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


}
