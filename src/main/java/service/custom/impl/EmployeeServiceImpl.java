package service.custom.impl;

import Entity.EmployeeEntity;
import com.google.inject.Inject;
import controller.products.ProductsController;
import model.Employee;
import model.EmployeeSales;
import model.Product;
import org.modelmapper.ModelMapper;
import repository.DaoFactory;
import repository.custom.EmployeeDao;
import service.custom.EmployeeService;
import util.DaoType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeServiceImpl implements EmployeeService {

    @Inject
    EmployeeDao dao;

    ModelMapper modelMapper = new ModelMapper();
    @Override
    public List<Integer> getEmpIds(){
        try {
            List<Integer> empIds = dao.getEmpIds();
            return empIds;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
        public List<EmployeeSales> getTopEmployees() {
        try {
            return dao.getTopEmployees();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Employee> getAll() {
        try {
            List<EmployeeEntity> employeeEntities = dao.getAll();
            List<Employee> employeeList = new ArrayList<>();

            employeeEntities.forEach(employee -> {
                employeeList.add(modelMapper.map(employee,Employee.class));
            });
            return employeeList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deletEmployee(Integer id) {
        return dao.delete(id);

    }

    @Override
    public boolean addEmployee(Employee employee) {
        try {
            return dao.save(modelMapper.map(employee, EmployeeEntity.class));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        return dao.update(employee.getId(),modelMapper.map(employee, EmployeeEntity.class));
    }

}
