package service;

import service.custom.impl.CartServiceImpl;
import service.custom.impl.CustomerServiceImpl;
import service.custom.impl.UserServiceImpl;
import util.ServiceType;

public class ServiceFactory {
    private static ServiceFactory instance;

    public static ServiceFactory getInstance(){
      return instance==null?instance=new ServiceFactory():instance;
    }

    public <T extends SuperService>T getServiceType(ServiceType serviceType){
      switch (serviceType){
          case CART: return (T) new CartServiceImpl();
          case CUSTOMER:return (T) new CustomerServiceImpl();
          case USER:return (T) new UserServiceImpl();

      }
      return null;
    }
}
