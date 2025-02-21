package repository.custom;

import model.Employee;
import model.EmployeeSales;
import repository.CrudDao;

import java.sql.SQLException;
import java.util.List;

public interface EmployeeDao extends CrudDao<Employee,Integer> {

    List<Integer> getEmpIds() throws SQLException;

    List<EmployeeSales> getTopEmployees() throws SQLException;

}
