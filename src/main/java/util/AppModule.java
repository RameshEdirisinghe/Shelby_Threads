package util;

import com.google.inject.AbstractModule;
import model.User;
import repository.custom.EmployeeDao;
import repository.custom.OrderDao;
import repository.custom.UserDao;
import repository.custom.impl.EmployeeDaoImpl;
import repository.custom.impl.OrderDaoImpl;
import repository.custom.impl.UserDaoImpl;
import service.custom.CartService;
import service.custom.EmployeeService;
import service.custom.OrderService;
import service.custom.UserService;
import service.custom.impl.CartServiceImpl;
import service.custom.impl.EmployeeServiceImpl;
import service.custom.impl.OrderServiceImpl;
import service.custom.impl.UserServiceImpl;

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
    }
}
