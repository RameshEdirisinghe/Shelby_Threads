package util;

import com.google.inject.AbstractModule;
import model.User;
import repository.custom.*;
import repository.custom.impl.*;
import service.custom.*;
import service.custom.impl.*;

public class AppModule extends AbstractModule{

    @Override
    protected void configure() {
        bind(CartService.class).to(CartServiceImpl.class);
        bind(EmployeeService.class).to(EmployeeServiceImpl.class);
        bind(OrderService.class).to(OrderServiceImpl.class);
        bind(EmployeeDao.class).to(EmployeeDaoImpl.class);
        bind(UserService.class).to(UserServiceImpl.class);
        bind(UserDao.class).to(UserDaoImpl.class);
        bind(OrderDao.class).to(OrderDaoImpl.class);
        bind(EmployeeService.class).to(EmployeeServiceImpl.class);
        bind(HomeService.class).to(HomeServiceImpl.class);
        bind(HomeDao.class).to(HomeDaoImpl.class);
        bind(CustomerService.class).to(CustomerServiceImpl.class);
        bind(SupplierService.class).to(SupplierServiceImpl.class);
        bind(SupplierDao.class).to(SupplierDaoImpl.class);
        bind(ProductDao.class).to(ProductDaoImpl.class);
        bind(ProductService.class).to(ProductServiceImpl.class);
    }
}
