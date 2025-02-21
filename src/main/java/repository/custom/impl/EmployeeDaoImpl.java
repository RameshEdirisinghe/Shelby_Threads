package repository.custom.impl;

import DBConnection.DBConnection;
import controller.user.UserController;
import model.Employee;
import model.EmployeeSales;
import repository.custom.EmployeeDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDaoImpl implements EmployeeDao {
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
    public boolean save(Employee entity) {
        return false;
    }

    @Override
    public boolean update(Integer s, Employee entity) {
        return false;
    }

    @Override
    public boolean delete(Integer s) {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);
            ResultSet rst = connection.createStatement().executeQuery("Select name from employee WHERE EmployeeID=?");

            PreparedStatement stm = connection.prepareStatement("DELETE FROM employee WHERE EmployeeID=?");
            stm.setInt(1, s);

            int rowsAffected = stm.executeUpdate();

            if (rowsAffected > 0) {
                if (rst.next()) {
                    boolean isUpdate = UserController.getInstance().deleteUser(rst.getString(1));
                    if (isUpdate) {
                        connection.commit();
                        return true;
                    }
                }
            }
            connection.rollback();
            return false;
        } catch (SQLException e) {
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
    public List<Employee> getAll() throws SQLException {

        List<Employee> employeeList = new ArrayList<>();
        ResultSet rst = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT * FROM Employee");
        while (rst.next()) {
            employeeList.add(new Employee(rst.getInt(1), rst.getString(2), rst.getString(3), "null", rst.getString(4)));
        }
        return employeeList;
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
