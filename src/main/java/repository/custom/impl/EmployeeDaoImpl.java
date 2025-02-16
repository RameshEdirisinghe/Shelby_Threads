package repository.custom.impl;

import DBConnection.DBConnection;
import model.Employee;
import repository.custom.EmployeeDao;

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
    public boolean update(String s, Employee entity) {
        return false;
    }

    @Override
    public boolean delete(String s) {
        return false;
    }

    @Override
    public List<Employee> getAll() {
        return List.of();
    }
}
