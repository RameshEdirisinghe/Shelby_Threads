package repository;

import repository.custom.impl.EmployeeDaoImpl;
import repository.custom.impl.OrderDaoImpl;
import util.DaoType;


public class DaoFactory {

    private static DaoFactory instance;

    public static DaoFactory getInstance(){
        return instance==null?instance=new DaoFactory():instance;
    }

    public <T extends SuperDao>T getDaoType(DaoType daoType){
        switch (daoType){
            case EMPLOYEE: return (T) new EmployeeDaoImpl();
            case ORDER: return (T) new OrderDaoImpl();
        }
        return null;
    }
}
