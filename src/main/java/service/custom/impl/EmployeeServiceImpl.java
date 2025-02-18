package service.custom.impl;

import com.google.inject.Inject;
import controller.products.ProductsController;
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
    public List<Integer> getEmpIds() throws SQLException {

        List<Integer> empIds = dao.getEmpIds();
        return empIds;

    }
}
