package repository.custom;

import model.Employee;
import repository.CrudDao;

import java.sql.SQLException;
import java.util.List;

public interface EmployeeDao extends CrudDao<Employee,String> {

    List<Integer> getEmpIds() throws SQLException;

}
