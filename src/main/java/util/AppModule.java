package util;

import com.google.inject.AbstractModule;
import repository.custom.EmployeeDao;
import repository.custom.impl.EmployeeDaoImpl;
import service.custom.CartService;
import service.custom.EmployeeService;
import service.custom.OrderService;
import service.custom.impl.CartServiceImpl;
import service.custom.impl.EmployeeServiceImpl;
import service.custom.impl.OrderServiceImpl;

public class AppModule extends AbstractModule{

    @Override
    protected void configure() {
        bind(CartService.class).to(CartServiceImpl.class);
        bind(EmployeeService.class).to(EmployeeServiceImpl.class);
        bind(OrderService.class).to(OrderServiceImpl.class);
        bind(EmployeeDao.class).to(EmployeeDaoImpl.class);
    }
}
