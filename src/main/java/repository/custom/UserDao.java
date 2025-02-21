package repository.custom;

import Entity.EmployeeEntity;
import Entity.UserEntity;
import model.Employee;
import repository.CrudDao;

import java.sql.SQLException;

public interface UserDao extends CrudDao<UserEntity,Integer> {
    UserEntity LoginAuthenticate(String email,String password) throws SQLException;

    boolean isExist(String email) throws SQLException;

    boolean updatePassword(String email,String newPassword) throws SQLException;

    boolean deleteUser(String userName) throws SQLException;

    boolean addUser(EmployeeEntity employee) throws SQLException;

    boolean updateUser(EmployeeEntity employee) throws SQLException;

}
