package repository.custom.impl;

import DBConnection.DBConnection;
import Entity.EmployeeEntity;
import com.google.inject.Inject;
import model.EmployeeSales;
import repository.custom.EmployeeDao;
import repository.custom.UserDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDaoImpl implements EmployeeDao {

    @Inject
    UserDao userDao;

    @Override
    public List<Integer> getEmpIds() throws SQLException {
        List<Integer> employeeList = new ArrayList<>();

        ResultSet rst = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT EmployeeID FROM Employee");
        while (rst.next()) {
            employeeList.add(rst.getInt(1));
        }
        return employeeList;
    }

    @Override
    public boolean save(EmployeeEntity entity) {
        Connection connection = DBConnection.getInstance().getConnection();
        try {

            connection.setAutoCommit(false);

            PreparedStatement stm = connection.prepareStatement("Insert Into Employee(Name,Email,company) values (?,?,?)");
            stm.setString(1,entity.getName());
            stm.setString(2,entity.getEmail());
            stm.setString(3,entity.getCompany());

            if (stm.executeUpdate()>0){
                boolean isAdd = userDao.addUser(entity);
                if (isAdd){
                    connection.commit();
                    return true;
                }
            }

            connection.rollback();
            return false;
        } catch (SQLException e) {

            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException("RollBack-Error"+ex);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


    }

    @Override
    public boolean update(Integer s, EmployeeEntity entity) {


        Connection connection = DBConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);

            PreparedStatement stm = connection.prepareStatement("UPDATE employee SET Name = ?, Email = ?, company = ? WHERE employeeid =?;");
            stm.setString(1,entity.getName());
            stm.setString(2,entity.getEmail());
            stm.setString(3,entity.getCompany());
            stm.setInt(4,entity.getId());

            if (stm.executeUpdate()>0){
                boolean isUpdate = userDao.updateUser(entity);
                if (isUpdate){
                    connection.commit();
                    return true;
                }
            }

            connection.rollback();
            return false;
        } catch (SQLException e) {
            try {
                connection.rollback(); // Rollback on any error
            } catch (SQLException ex) {
                throw new RuntimeException("Rollback failed", ex);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public boolean delete(Integer s) {
        Connection connection = DBConnection.getInstance().getConnection();
        try {

            String employeeName = null;
            try (PreparedStatement selectStm = connection.prepareStatement("SELECT name FROM employee WHERE EmployeeID=?")) {
                selectStm.setInt(1, s);
                try (ResultSet rst = selectStm.executeQuery()) {
                    if (rst.next()) {
                        employeeName = rst.getString(1);
                    } else {
                        return false;
                    }
                }
            }

            connection.setAutoCommit(false);

            // Delete from the employee table
            try (PreparedStatement deleteStm = connection.prepareStatement("DELETE FROM employee WHERE EmployeeID=?")) {
                deleteStm.setInt(1, s);
                int rowsAffected = deleteStm.executeUpdate();

                if (rowsAffected > 0) {
                    // Delete related user from the user table
                    boolean isUpdate = userDao.deleteUser(employeeName);
                    if (isUpdate) {
                        connection.commit(); // Commit transaction if both deletes succeed
                        return true;
                    }
                }
            }

            connection.rollback(); // Rollback if user deletion fails
            return false;
        } catch (SQLException e) {
            try {
                connection.rollback(); // Rollback on any error
            } catch (SQLException ex) {
                throw new RuntimeException("Rollback failed", ex);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public List<EmployeeEntity> getAll() throws SQLException {

        List<EmployeeEntity> employeeList = new ArrayList<>();
        ResultSet rst = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT * FROM Employee");
        while (rst.next()) {
            employeeList.add(new EmployeeEntity(rst.getInt(1), rst.getString(2), rst.getString(3), "null", rst.getString(4)));
        }
        return employeeList;
    }

    @Override
    public EmployeeEntity search(Integer integer) {
        try {
            String query = "SELECT * FROM employee WHERE EmployeeID=?";
            PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement(query);
            stmt.setString(1, integer+""); // Set empId parameter
            ResultSet rst = stmt.executeQuery();

            if (rst.next()) {
                return new EmployeeEntity(rst.getInt(1), rst.getString(2), rst.getString(3), "null", rst.getString(4));
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<EmployeeSales> getTopEmployees() throws SQLException {
        String query = "SELECT e.Name AS EmployeeName, SUM(o.TotalCost) AS TotalSales\n" +
                "FROM orders o\n" +
                "JOIN employee e ON o.EmployeeID = e.EmployeeID\n" +
                "GROUP BY e.Name\n" +
                "ORDER BY TotalSales DESC;";

        List<EmployeeSales> employeeSalesList = new ArrayList<>();


        PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            String employeeName = resultSet.getString("EmployeeName");
            double totalSales = resultSet.getDouble("TotalSales");

            EmployeeSales employeeSales = new EmployeeSales(employeeName, totalSales);
            employeeSalesList.add(employeeSales);
        }


        return employeeSalesList;
    }

}
