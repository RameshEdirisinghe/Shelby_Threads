package service.custom.impl;

import com.google.inject.Inject;
import controller.products.ProductsController;
import model.Employee;
import model.EmployeeSales;
import model.Product;
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
            return dao.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deletEmployee() {

        return false;
    }

}
