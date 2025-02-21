package repository.custom.impl;

import DBConnection.DBConnection;
import model.Supplier;
import repository.custom.SupplierDao;

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
    public boolean save(Supplier entity) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Integer integer, Supplier entity) {
        return false;
    }

    @Override
    public boolean delete(Integer integer) {
        return false;
    }

    @Override
    public List<Supplier> getAll() throws SQLException {

        List<Supplier> supplierList = new ArrayList<>();
        ResultSet rst = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT * FROM supplier");
        while (rst.next()) {
            supplierList.add(new Supplier(rst.getInt(1), rst.getString(2), rst.getString(4), rst.getString(3), rst.getString(5)));
            System.out.println(supplierList);
        }
        return supplierList;
    }
}
