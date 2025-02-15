package service;

import service.custom.impl.CartServiceImpl;
import util.ServiceType;

public class ServiceFactory {
    private static ServiceFactory instance;

    public static ServiceFactory getInstance(){
      return instance==null?instance=new ServiceFactory():instance;
    }

    public <T extends SuperService>T getServiceType(ServiceType serviceType){
      switch (serviceType){
          case CART: return (T) new CartServiceImpl();
      }
      return null;
    }
}
