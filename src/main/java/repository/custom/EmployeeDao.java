package repository.custom;

import Entity.EmployeeEntity;
import model.Employee;
import model.EmployeeSales;
import repository.CrudDao;

import java.sql.SQLException;
import java.util.List;

public interface EmployeeDao extends CrudDao<EmployeeEntity,Integer> {

    List<Integer> getEmpIds() throws SQLException;

    List<EmployeeSales> getTopEmployees() throws SQLException;

}
