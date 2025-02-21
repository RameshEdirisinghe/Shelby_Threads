package repository.custom.impl;

import DBConnection.DBConnection;
import Entity.SupplierEntity;
import model.Supplier;
import repository.custom.SupplierDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierDaoImpl implements SupplierDao {

    @Override
    public int getAllSupplierCount() throws SQLException {
        ResultSet rst = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT COUNT(*) AS total_suppliers FROM supplier;");
        while (rst.next()) {
            return (rst.getInt(1));
        }
        return 0;
    }

    @Override
    public List<String> getAllSupplierNames() throws SQLException {
        List<String> supplierList = new ArrayList<>();
        ResultSet rst = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT name FROM Supplier");
        while (rst.next()) {
            supplierList.add(rst.getString(1));
        }
        return supplierList;
    }

    @Override
    public boolean save(SupplierEntity entity) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement stm = connection.prepareStatement("Insert Into Supplier(Name,Email,company,Item) values (?,?,?,?)");
            stm.setString(1, entity.getName());
            stm.setString(2, entity.getEmail());
            stm.setString(3, entity.getCompany());
            stm.setString(4, entity.getItem());

            return stm.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(Integer integer, SupplierEntity entity) {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement stm = connection.prepareStatement("UPDATE supplier SET Name = ?, Email = ?, company = ?,Item =? WHERE SupplierID =?;");
            stm.setString(1, entity.getName());
            stm.setString(2, entity.getEmail());
            stm.setString(3, entity.getCompany());
            stm.setString(4, entity.getItem());
            stm.setInt(5, integer);

            return stm.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(Integer integer) {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement stm = connection.prepareStatement("DELETE FROM supplier WHERE SupplierID=?");
            stm.setInt(1, integer);
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SupplierEntity> getAll() throws SQLException {

        List<SupplierEntity> supplierList = new ArrayList<>();
        ResultSet rst = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT * FROM supplier");
        while (rst.next()) {
            supplierList.add(new SupplierEntity(rst.getInt(1), rst.getString(2), rst.getString(4), rst.getString(3), rst.getString(5)));
            System.out.println(supplierList);
        }
        return supplierList;
    }

    @Override
    public SupplierEntity search(Integer integer) throws SQLException {
        String query = "SELECT * FROM supplier WHERE SupplierID=?";
        PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement(query);
        stmt.setInt(1, integer); // Set empId parameter
        ResultSet rst = stmt.executeQuery();

        if (rst.next()) {
            return new SupplierEntity(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5));
        }
        return null;
    }
}
