package service.custom;

import model.Product;

import java.sql.SQLException;
import java.util.List;

public interface EmployeeService {

    List<Integer> getEmpIds() throws SQLException;
}
