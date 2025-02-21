package service.custom;

import model.Employee;
import model.EmployeeSales;
import model.Product;

import java.sql.SQLException;
import java.util.List;

public interface EmployeeService {

    List<Integer> getEmpIds();
    List<EmployeeSales> getTopEmployees();
    List<Employee> getAll();
    boolean deletEmployee(Integer id);
    boolean addEmployee(Employee employee);
    boolean updateEmployee(Employee employee);
    Employee searchEmployee(Integer empId);
}
